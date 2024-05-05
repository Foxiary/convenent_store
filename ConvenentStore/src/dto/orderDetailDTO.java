package dto;

import java.util.Date;

public class orderDetailDTO {

    private Integer orderID;
    private Integer productId;
    private String type;
    private String brand;
    private String productName;
    private Integer quantity;
    private Double price;
    private Date date;

    /**
     *
     * @param orderID
     * @param type
     * @param brand
     * @param productId 
     * @param productName
     * @param quantity
     * @param price
     * @param date
     */
    public orderDetailDTO(Integer orderID, String type, String brand, Integer productId, String productName, Integer quantity, Double price, Date date) {
        this.orderID = orderID;
        this.type = type;
        this.brand = brand;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public orderDetailDTO(Integer customerID, String type, String brand, String productName, Integer quantity, Double price, Date date) {
        this.orderID = orderID;
        this.type = type;
        this.brand = brand;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
}