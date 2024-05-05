package controller;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
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

public class dashboardController implements Initializable {
    @FXML
    private TextField orders_productID;
    
    @FXML
    private TextField orders_amount;

    @FXML
    private Button orders_addBtn;

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private TextField addProducts_brand;

    @FXML
    private Button addProducts_btn;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_brand;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_price;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_productID;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_productName;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_status;
    
    @FXML
    private TableColumn<productDTO, String> addProducts_col_type;

    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private ImageView addProducts_imageView;

    @FXML
    private Button addProducts_importBtn;

    @FXML
    private TextField addProducts_price;

    @FXML
    private TextField addProducts_productID;

    @FXML
    private TextField addProducts_productName;

    @FXML
    private ComboBox<?> addProducts_productType;

    @FXML
    private ComboBox<?> addProducts_status;

    @FXML
    private Button addProducts_resetBtn;

    @FXML
    private TextField addProducts_search;

    @FXML
    private TableView<productDTO> addProducts_table;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProducts;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AreaChart<?, ?> home_incomeChart;

    @FXML
    private Label home_numberOrder;

    @FXML
    private BarChart<?, ?> home_orderChart;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Label orders_balance;

    @FXML
    private Button orders_btn;

    @FXML
    private TableView<orderDetailDTO> orders_tableView;

    @FXML
    private TableColumn<orderDetailDTO, String> orders_col_brand;

    @FXML
    private TableColumn<orderDetailDTO, String> orders_col_price;

    @FXML
    private TableColumn<orderDetailDTO, String> orders_col_productName;

    @FXML
    private TableColumn<orderDetailDTO, String> orders_col_quantity;

    @FXML
    private TableColumn<orderDetailDTO, String> orders_col_type;

    @FXML
    private AnchorPane orders_form;

    @FXML
    private Button orders_payBtn;

    @FXML
    private ComboBox<?> orders_brand;

    @FXML
    private ComboBox<?> orders_productType;

    @FXML
    private ComboBox<?> orders_productName;

    @FXML
    private Spinner<Integer> orders_quantity;

    @FXML
    private Button orders_receiptBtn;

    @FXML
    private Button orders_resetBtn;

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

    public void homeIncomeChart() {
        home_incomeChart.getData().clear();
        String sql = "SELECT TOP 6 date, SUM(total) AS totalIncome FROM orders GROUP BY date ORDER BY date ASC;";
        connect = database.connectDb();
        try {
            XYChart.Series chart = new XYChart.Series();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_incomeChart.getData().add(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeOrdersChart() {
        home_orderChart.getData().clear();
        String sql = "SELECT TOP 5 date, COUNT(*) AS totalOrders FROM order_detail GROUP BY date ORDER BY date ASC;";
        connect = database.connectDb();
        try {
            XYChart.Series chart = new XYChart.Series();
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            home_orderChart.getData().add(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkConstraints() throws SQLException {
        Alert alert;
        if (addProducts_productID.getText().isEmpty()
                || addProducts_productType.getSelectionModel().getSelectedItem() == null
                || addProducts_brand.getText().isEmpty()
                || addProducts_productName.getText().isEmpty()
                || addProducts_price.getText().isEmpty()
                || addProducts_status.getSelectionModel().getSelectedItem() == null
                || getData.path == null) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return false;
        } else {
            // CHECK IF THE PRODUCT ID AND PRICE ARE NUMBERS
            try {
                Integer.parseInt(addProducts_productID.getText());
                Double.parseDouble(addProducts_price.getText());
            } catch (NumberFormatException e) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Product ID and Price must be numbers");
                alert.showAndWait();
                return false;
            }
            // CHECK IF THE PRODUCT NAME AND BRAND START WITH UPPERCASE LETTER
            if (!Character.isUpperCase(addProducts_productName.getText().charAt(0)) || !Character.isUpperCase(addProducts_brand.getText().charAt(0))) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Product Name and Brand must start with an uppercase letter");
                alert.showAndWait();
                return false;
            }
            // CHECK IF THE PRODUCT ID IS ALREADY EXIST
            String checkData = "SELECT product_id FROM product WHERE product_id = '"
                    + addProducts_productID.getText() + "'";
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);
            if (result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Product ID: " + addProducts_productID.getText() + " was already exist!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
    
    
    public void addProductsAdd() throws SQLException {
        if (!checkConstraints()) {
            return;
        }
        String sql = "INSERT INTO product (product_id, type, brand, productName, price, status, image, date)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        connect = database.connectDb();
        try {
            String uri = getData.path;
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, addProducts_productID.getText());
            prepare.setString(2, (String) addProducts_productType.getSelectionModel().getSelectedItem());
            prepare.setString(3, addProducts_brand.getText());
            prepare.setString(4, addProducts_productName.getText());
            prepare.setString(5, addProducts_price.getText());
            prepare.setString(6, (String) addProducts_status.getSelectionModel().getSelectedItem());
    
            uri = uri.replace("\\", "\\\\");
            prepare.setString(7, uri);
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setString(8, String.valueOf(sqlDate));
    
            prepare.executeUpdate();
    
            //update table
            addProductsShowListData();
            // CLEAR THE FIELDS
            addProductsReset();
        } catch (SQLException e) {
        }
    }
    
    public void addProductsUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE product SET type = '"
                + addProducts_productType.getSelectionModel().getSelectedItem()
                + "', brand = '" + addProducts_brand.getText()
                + "', productName = '" + addProducts_productName.getText()
                + "', price = '" + addProducts_price.getText()
                + "', status = '" + addProducts_status.getSelectionModel().getSelectedItem()
                + "', image = '" + uri + "', date = '" + sqlDate + "' WHERE product_id = '"
                + addProducts_productID.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;
            if (addProducts_productID.getText().isEmpty()
                    || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty()
                    || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE? Product ID: " + addProducts_productID.getText() + "");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductsReset();
                }
            }
        } catch (SQLException e) {
        }
    }

    public void addProductsDelete() {
        String sql = "DELETE FROM product WHERE product_id = '" + addProducts_productID.getText() + "'";
        connect = database.connectDb();
        Alert alert;

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Product ID: " + addProducts_productID.getText() + "");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            try {
                statement = connect.createStatement();
                statement.executeUpdate(sql);
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted");
                alert.showAndWait();

                addProductsShowListData();
                addProductsReset();

            } catch (SQLException ex) {
                Logger.getLogger(dashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addProductsReset() {
        addProducts_productID.setText("");
        addProducts_productType.getSelectionModel().clearSelection();
        addProducts_brand.setText("");
        addProducts_productName.setText("");
        addProducts_price.setText("");
        addProducts_status.getSelectionModel().clearSelection();
        addProducts_imageView.setImage(null);
        getData.path = "";
    }

    public void addProductsImportImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*jpg", "*png"));
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 133, 155, false, true);
            addProducts_imageView.setImage(image);
        }
    }

    public void addProductsSelect() {
        productDTO prodD = addProducts_table.getSelectionModel().getSelectedItem();
        int num = addProducts_table.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }

        addProducts_productID.setText(String.valueOf(prodD.getProductId()));
        addProducts_brand.setText(prodD.getBrand());
        addProducts_productName.setText(prodD.getProductName());
        addProducts_price.setText(String.valueOf(prodD.getPrice()));

        String uri = "file:" + prodD.getImage();
        System.out.println(uri);

        image = new Image(uri, 133, 155, false, true);
        addProducts_imageView.setImage(image);
        getData.path = prodD.getImage();
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

    public void addProductsSearch() {
        FilteredList<productDTO> filter = new FilteredList<>(addProductsList, e -> true);
        addProducts_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateProductDTO -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();
                if (predicateProductDTO.getProductId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateProductDTO.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductDTO.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductDTO.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(predicateProductDTO.getPrice()).contains(searchKey)) {
                    return true;
                } else if (predicateProductDTO.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<productDTO> sortList = new SortedList<>(filter);
            sortList.comparatorProperty().bind(addProducts_table.comparatorProperty());
            addProducts_table.setItems(sortList);
        });
    }

    private ObservableList<productDTO> addProductsList;

    public void addProductsShowListData() {
        addProductsList = addProductsListData();
        addProducts_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addProducts_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProducts_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addProducts_table.setItems(addProductsList);
    }

    private double x = 0;
    private double y = 0;

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

    private String[] listType = {"Snacks", "Drinks", "Dessert", "Gadgets", "Personal Product", "Others"};

    public void addProductsListType() {
        List<String> listT = new ArrayList<>();
        for (String data : listType) {
            listT.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listT);
        addProducts_productType.setItems(listData);
    }

    private String[] listStatus = {"Available", "Not Available"};

    public void addProductsListStatus() {
        List<String> listS = new ArrayList<>();

        for (String data : listStatus) {
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        addProducts_status.setItems(listData);
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
                int product_id = result.getInt("product_id");
                String type = result.getString("type");
                String brand = result.getString("brand");
                int quantity = result.getInt("quantity");
                double price = result.getDouble("price");
                Date date = result.getDate("date");
    
                // Fetch the product name from the product table using the product_id
                String productNameSql = "SELECT productName FROM product WHERE product_id = " + product_id;
                Statement productNameStatement = connect.createStatement();
                ResultSet productNameResult = productNameStatement.executeQuery(productNameSql);
                String productName = null;
                if (productNameResult.next()) {
                    productName = productNameResult.getString("productName");
                }
    
                orderD = new orderDetailDTO(orderid, type, brand, productName, quantity, price, date);
                listData.add(orderD);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }    

    public void ordersSelect() {
        if (orders_tableView.getSelectionModel().getSelectedItem() != null) {
            orderDetailDTO selectedRow = orders_tableView.getSelectionModel().getSelectedItem();
            orders_productID.setText(String.valueOf(selectedRow.getProductId()));
        }
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
    String sql = "INSERT INTO order_detail (order_id, product_id, productName, type, brand, quantity, price, date) "
            + "VALUES(?,?,?,?,?,?,?,?)";

    connect = database.connectDb();

    try {
        String checkData = "SELECT * FROM product WHERE product_id = '"
                + orders_productID.getText() + "'";

        double priceData = 0;
        String type = null;
        String brand = null;
        String productName = null;
        int productId = 0;

        statement = connect.createStatement();
        result = statement.executeQuery(checkData);

        if (result.next()) {
            priceData = result.getDouble("price");
            type = result.getString("type");
            brand = result.getString("brand");
            productId = result.getInt("product_id");
            productName = result.getString("productName");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Product ID does not exist in the database");
            alert.showAndWait();
            return;
        }

        double totalPData = (priceData * qty);

        Alert alert;
        if (orders_productID.getText()== null || totalPData == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose product first and the quantity > 0");
            alert.showAndWait();
        } else {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, String.valueOf(orderid));
            prepare.setInt(2, productId);
            prepare.setString(3, String.valueOf(productName));
            prepare.setString(4, String.valueOf(type));
            prepare.setString(5, String.valueOf(brand));
            prepare.setString(6, String.valueOf(qty));

            prepare.setString(7, String.valueOf(totalPData)); 

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setString(8, String.valueOf(sqlDate));

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
    Document document = new Document();
    try {
        PdfWriter.getInstance(document, new FileOutputStream("OrderDetails.pdf"));
        document.open();

        // Thêm tiêu đề
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 24, BaseColor.BLACK);
        Chunk title = new Chunk("Receipt", font);
        Paragraph p = new Paragraph(title);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));
        document.add(new Paragraph(""));

        PdfPTable table = new PdfPTable(5); // 5 columns.

        PdfPCell cell1 = new PdfPCell(new Phrase("Type"));
        PdfPCell cell2 = new PdfPCell(new Phrase("Brand"));
        PdfPCell cell3 = new PdfPCell(new Phrase("Product Name"));
        PdfPCell cell4 = new PdfPCell(new Phrase("Quantity"));
        PdfPCell cell5 = new PdfPCell(new Phrase("Price"));

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);

        for (orderDetailDTO order : ordersList) {
            table.addCell(order.getType());
            table.addCell(order.getBrand());
            table.addCell(order.getProductName());
            table.addCell(String.valueOf(order.getQuantity()));
            table.addCell(String.valueOf(order.getPrice()));
        }

        document.add(table);

        // Hiển thị text của orderTotal
        Paragraph totalParagraph = new Paragraph("Total: " + totalP);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.close();

        System.out.println("PDF Created!");
    } catch (Exception e) {
        e.printStackTrace();
    }
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
            addProducts_form.setVisible(false);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            addProducts_btn.setStyle("-fx-background-color:transparent");
            orders_btn.setStyle("-fx-background-color:transparent");

            homeDisplayTotalOrders();
            homeTotalIncome();
            homeAvailableProducts();

            homeIncomeChart();
            homeOrdersChart();
        } else if (event.getSource() == addProducts_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(true);
            orders_form.setVisible(false);

            addProducts_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            home_btn.setStyle("-fx-background-color:transparent");
            orders_btn.setStyle("-fx-background-color:transparent");

            addProductsShowListData();
            addProductsListStatus();
            addProductsListType();
            addProductsSearch();
        } else if (event.getSource() == orders_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(false);
            orders_form.setVisible(true);

            orders_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            addProducts_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");

            ordersShowListData();
            ordersSpinner();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        defaultNav();

        homeDisplayTotalOrders();
        homeTotalIncome();
        homeAvailableProducts();
        homeIncomeChart();
        homeOrdersChart();

        //show data on table
        addProductsShowListData();
        addProductsListStatus();
        addProductsListType();

        ordersShowListData();
        ordersSpinner();
    }
}
