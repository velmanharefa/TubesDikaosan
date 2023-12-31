package com.TubesDiKaosan.ecommerce.payloads.requests;

public class StockProductRequest {
    private Integer id;
    private String size;
    private Integer quantity;
    private String color;

    public StockProductRequest() {
    }

    public StockProductRequest(Integer id,String size, Integer quantity, String color) {
        this.size = size;
        this.quantity = quantity;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id= id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
