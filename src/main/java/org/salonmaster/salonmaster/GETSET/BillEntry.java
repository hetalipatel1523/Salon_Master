package org.salonmaster.salonmaster.GETSET;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BillEntry {
    private final IntegerProperty invoiceNo;
    private final StringProperty custNum;
    private final StringProperty item;
    private final StringProperty paymentMethod;
    private final StringProperty date;
    private final StringProperty stock;
    private final IntegerProperty price;

    public BillEntry(int invoiceNo, String custNum, String item, String paymentMethod, String date, String stock, int price) {
        this.invoiceNo = new SimpleIntegerProperty(invoiceNo);
        this.custNum = new SimpleStringProperty(custNum);
        this.item = new SimpleStringProperty(item);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.date = new SimpleStringProperty(date);
        this.stock = new SimpleStringProperty(stock);
        this.price = new SimpleIntegerProperty(price);
    }

    // Getters and Setters for invoiceNo
    public Integer getInvoiceNo() {
        return invoiceNo.get();
    }

    public IntegerProperty invoiceNoProperty() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo.set(invoiceNo);
    }

    // Getters and Setters for custNum
    public String getCustNum() {
        return custNum.get();
    }

    public StringProperty custNumProperty() {
        return custNum;
    }

    public void setCustNum(String custNum) {
        this.custNum.set(custNum);
    }

    // Getters and Setters for item
    public String getItem() {
        return item.get();
    }

    public StringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    // Getters and Setters for paymentMethod
    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    // Getters and Setters for date
    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    // Getters and Setters for stock
    public String getStock() {
        return stock.get();
    }

    public StringProperty stockProperty() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock.set(stock);
    }

    // Getters and Setters for price
    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }
}
