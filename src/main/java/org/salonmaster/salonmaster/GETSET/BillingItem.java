package org.salonmaster.salonmaster.GETSET;

public class BillingItem {
    private int serialNumber;
    private String item;
    private Integer price;
    private String stock;
    private String category;
    private String ps;

    public BillingItem(int serialNumber, String item, Integer price, String stock, String category, String ps) {
        this.serialNumber = serialNumber;
        this.item = item;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.ps = ps;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

   
}