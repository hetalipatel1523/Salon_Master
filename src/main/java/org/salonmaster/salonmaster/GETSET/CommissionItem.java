package org.salonmaster.salonmaster.GETSET;

public class CommissionItem {
    private String invoiceNo;
    private String employeeName;
    private String employeeNumber;
    private double price;
    private double commission;

    public CommissionItem(String invoiceNo, String employeeName, String employeeNumber, double price, double commission) {
        this.invoiceNo = invoiceNo;
        this.employeeName = employeeName;
        this.employeeNumber = employeeNumber;
        this.price = price;
        this.commission = commission;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
