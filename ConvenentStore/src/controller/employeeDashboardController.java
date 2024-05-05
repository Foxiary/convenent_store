package controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

//import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseEvent;

import dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.database;
import util.getData;

/**
 * FXML Controller class
 *
 * @author tieut
 */
public class employeeDashboardController implements Initializable {

    private double x = 0;
    private double y = 0;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProducts;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_numberOrder;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Button orders_addBtn;

    @FXML
    private TextField orders_amount;

    @FXML
    private Label orders_balance;

    @FXML
    private ComboBox<?> orders_brand;

    @FXML
    private Button orders_btn;

    @FXML
    private TableColumn<productDTO, String> orders_col_brand;

    @FXML
    private TableColumn<productDTO, String> orders_col_price;

    @FXML
    private TableColumn<productDTO, String> orders_col_productName;

    @FXML
    private TableColumn<productDTO, String> orders_col_quantity;

    @FXML
    private TableColumn<productDTO, String> orders_col_type;

    @FXML
    private AnchorPane orders_form;

    @FXML
    private Button orders_payBtn;

    @FXML
    private ComboBox<?> orders_productName;

    @FXML
    private ComboBox<?> orders_productType;

    @FXML
    private Spinner<Integer> orders_quantity;

    @FXML
    private Button orders_receiptBtn;

    @FXML
    private Button orders_resetBtn;

    @FXML
    private TableView<orderDetailDTO> orders_tableView;

    @FXML
    private Label orders_total;

    @FXML
    private Label username;

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    public void defaultNav() {
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
    }

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void homeDisplayTotalOrders() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT COUNT (*) AS ordersCount FROM order_detail WHERE date = '" + sqlDate + "'";
        connect = database.connectDb();

        int countOrders = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                countOrders = result.getInt("ordersCount");
            }

            home_numberOrder.setText((String.valueOf(countOrders)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void homeTotalIncome() {
        String sql = "SELECT SUM(total) AS totalIncome FROM orders";
        connect = database.connectDb();
        double totalIncome = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                totalIncome = result.getDouble("totalIncome");
            }

            home_totalIncome.setText(String.valueOf(totalIncome) + " VND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeAvailableProducts() {
        String sql = "SELECT COUNT (product_id) AS productCount FROM product WHERE status = 'Available'";
        connect = database.connectDb();

        int countAP = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                countAP = result.getInt("productCount");
            }

            home_availableProducts.setText((String.valueOf(countAP)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<productDTO> addProductsListData() {
        ObservableList<productDTO> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            productDTO prodD;
            while (result.next()) {
                prodD = new productDTO(result.getInt("product_id"),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));
                productList.add(prodD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                // LINK TO LOGIN FORM 
                root.setOnMousePressed((MouseEvent mouseEvent) -> {
                    x = mouseEvent.getSceneX();
                    y = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                    stage.setX(mouseEvent.getScreenX() - x);
                    stage.setY(mouseEvent.getScreenY() - y);
                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased((MouseEvent mouseEvent) -> stage.setOpacity(1));

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    

//------------------------------------------------------------------------------
    //orderData = orderDetailDTO
    public ObservableList<orderDetailDTO> ordersListData() {
        orderId();
        ObservableList<orderDetailDTO> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM order_detail WHERE order_id = '" + orderid + "'";

        connect = database.connectDb();

        try {
            orderDetailDTO orderD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                orderD = new orderDetailDTO(result.getInt("order_id"),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getDate("date"));
                listData.add(orderD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private SpinnerValueFactory<Integer> spinner;

    public void ordersSpinner() {
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);

        orders_quantity.setValueFactory(spinner);
    }

    private int qty;

    @FXML
    public void ordersShowSpinnerValue() {
        qty = orders_quantity.getValue();
    }

    @FXML
    public void ordersAdd() throws SQLException {
        orderId();
        String sql = "INSERT INTO order_detail (order_id, type, brand, productName, quantity, price, date) "
                + "VALUES(?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            String checkData = "SELECT * FROM product WHERE productName = '"
                    + orders_productName.getSelectionModel().getSelectedItem() + "'";

            double priceData = 0;

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            if (result.next()) {
                priceData = result.getDouble("price");
            }
            double totalPData = (priceData * qty);

            Alert alert;
            if (orders_productType.getSelectionModel().getSelectedItem() == null
                    || (String) orders_brand.getSelectionModel().getSelectedItem() == null
                    || (String) orders_productName.getSelectionModel().getSelectedItem() == null
                    || totalPData == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose product first");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(orderid));
                prepare.setString(2, (String) orders_productType.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String) orders_brand.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String) orders_productName.getSelectionModel().getSelectedItem());
                prepare.setString(5, String.valueOf(qty));

                prepare.setString(6, String.valueOf(totalPData));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(7, String.valueOf(sqlDate));

                prepare.executeUpdate();

                ordersShowListData();
                ordersDisplayTotal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ordersPay() {
        String sql = "INSERT INTO orders (order_id, total, amount, balance, date)"
                + "VALUES (?, ?, ?, ?, ?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (totalP > 0 || orders_amount.getText().isEmpty() || amountP == 0) {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(orderid));
                    prepare.setString(2, String.valueOf(totalP));
                    prepare.setString(3, String.valueOf(amountP));
                    prepare.setString(4, String.valueOf(balanceP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(5, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully!");
                    alert.showAndWait();

                    ordersShowListData();

                    totalP = 0;
                    balanceP = 0;
                    amountP = 0;

                    orders_balance.setText("0 VND");
                    orders_amount.setText("");

                } else {
                    return;
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double amountP;
    private double balanceP;

    @FXML
    public void ordersAmount() {
        Alert alert;
    
        if (!orders_amount.getText().isEmpty()) {
            try {
                amountP = Double.parseDouble(orders_amount.getText());
                if (totalP > 0) {
                    if (amountP > totalP) {
                        balanceP = (amountP - totalP);
    
                        orders_balance.setText(String.valueOf(balanceP) + " VND");
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Not enough money!");
                        alert.showAndWait();
    
                        orders_amount.setText("");
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid :3");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please input a valid number");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please input a number");
            alert.showAndWait();
        }
    }
    

    @FXML
    public void orderReceipt() {

    }

    @FXML
    public void ordersReset() {
        orderId();
        String sql = "DELETE FROM order_detail WHERE order_id = '" + orderid + "'";
        connect = database.connectDb();
        try {
            if (!orders_tableView.getItems().isEmpty()) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to reset?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    ordersShowListData();

                    totalP = 0;
                    balanceP = 0;
                    amountP = 0;

                    orders_balance.setText("0 VND");
                    orders_total.setText("0 VND");
                    orders_amount.setText("");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double totalP;

    public void ordersDisplayTotal() {
        orderId();
        String sql = "SELECT SUM(price) AS total_price FROM order_detail WHERE order_id = '" + orderid + "'";

        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                totalP = result.getDouble("total_price");
            }

            orders_total.setText(String.valueOf(totalP) + " VND");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] orderListType = {"Snacks", "Drinks", "Dessert", "Gadgets", "Personal Product", "Others"};

    @FXML
    public void ordersListType() {
        List<String> listT = new ArrayList<>();
        for (String data : orderListType) {
            listT.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listT);
        orders_productType.setItems(listData);

        ordersListBrand();
    }

    public void ordersListProductName() {
        String sql = "SELECT productName FROM product WHERE brand = '"
                + orders_brand.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("productName"));
            }
            orders_productName.setItems(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ordersListBrand() {

        String sql = "SELECT brand FROM product WHERE type = '"
                + orders_productType.getSelectionModel().getSelectedItem()
                + "' and status = 'Available' GROUP BY brand";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("brand"));
            }
            orders_brand.setItems(listData);
            ordersListProductName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<orderDetailDTO> ordersList;

    public void ordersShowListData() {
        ordersList = ordersListData();
        orders_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        orders_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        orders_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orders_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        orders_tableView.setItems(ordersList);
        ordersDisplayTotal();
    }

    private int orderid;

    public void orderId() {
        String customId = "SELECT * FROM order_detail";
        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(customId);
            result = prepare.executeQuery();

            int checkId = 0;

            while (result.next()) {
                orderid = result.getInt("order_id");
            }

            String checkData = "SELECT * FROM orders";
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);
            while (result.next()) {
                checkId = result.getInt("order_id");
            }
            if (orderid == 0) {
                orderid += 1;
            } else if (checkId == orderid) {
                orderid += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            orders_btn.setStyle("-fx-background-color:transparent");

            homeDisplayTotalOrders();
            homeTotalIncome();
            homeAvailableProducts();
        
        } else if (event.getSource() == orders_btn) {
            home_form.setVisible(false);
            orders_form.setVisible(true);

            orders_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            home_btn.setStyle("-fx-background-color:transparent");

            ordersShowListData();
            ordersListType();
            ordersListBrand();
            ordersListProductName();
            ordersSpinner();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        defaultNav();

        homeDisplayTotalOrders();
        homeTotalIncome();
        homeAvailableProducts();

        ordersShowListData();
        ordersListType();
        ordersListBrand();
        ordersListProductName();
        ordersSpinner();
    }
}
