package org.salonmaster.salonmaster.GETSET;


public class ProductData {

    private Integer id;
    private String item;
    private Integer stock;
    private String price;
    private String category;
    private String ps;

    public ProductData(Integer id, String item, Integer stock, String price, String category, String ps) {
        this.id = id;
        this.item = item;
        this.stock = stock;
        this.price = price;
        this.category = category;
        this.ps = ps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
