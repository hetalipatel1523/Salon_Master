package org.salonmaster.salonmaster.Controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.CommissionItem;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class Commissiontable_controller implements Initializable {


    @FXML
    private Button Submit;


    @FXML
    private TableView<CommissionItem> Tablecl;


    @FXML
    private DatePicker enddate;


    @FXML
    private TextField searchfield;


    @FXML
    private DatePicker startdate;


    @FXML
    private Label totalamount;


    @FXML
    private Label employeecommission;


    @FXML
    private TableColumn<CommissionItem, String> invoiceno;


    @FXML
    private TableColumn<CommissionItem, String> name;


    @FXML
    private TableColumn<CommissionItem, String> number;


    @FXML
    private TableColumn<CommissionItem, Double> price;


    @FXML
    private TableColumn<CommissionItem, Double> ecommission;


    private Connection con;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    public void Back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = Connectionprovider.getConnection();


        // Set cell value factories for table columns
        invoiceno.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        name.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        number.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ecommission.setCellValueFactory(new PropertyValueFactory<>("commission"));


        // Fetch data and populate table
        Submit.setOnAction(event -> {
            try {
                String searchText = searchfield.getText();
                LocalDate startDate = startdate.getValue();
                LocalDate endDate = enddate.getValue();


                String query;
                PreparedStatement pst;


                if (startDate != null && endDate != null) {
                    // Query with date range
                    query = "SELECT invoice_no, ename, enumber, price, commission FROM ms_bl WHERE enumber = ? AND date BETWEEN ? AND ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, searchText);
                    pst.setObject(2, startDate);
                    pst.setObject(3, endDate);
                } else {
                    // Query without date range
                    query = "SELECT invoice_no, ename, enumber, price, commission FROM ms_bl WHERE enumber = ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, searchText);
                }


                ResultSet rs = pst.executeQuery();


                ObservableList<CommissionItem> commissionItems = FXCollections.observableArrayList();


                while (rs.next()) {
                    String invoiceNo = rs.getString("invoice_no");
                    String employeeName = rs.getString("ename");
                    String employeeNumber = rs.getString("enumber");
                    double price = rs.getDouble("price");
                    double commission = rs.getDouble("commission");


                    CommissionItem item = new CommissionItem(invoiceNo, employeeName, employeeNumber, price, commission);
                    commissionItems.add(item);
                }


                Tablecl.setItems(commissionItems);


                // Calculate total amount and commission
                double total = commissionItems.stream().mapToDouble(CommissionItem::getPrice).sum();
                totalamount.setText("Total Amount: " + total);


                double totalCommission = commissionItems.stream().mapToDouble(CommissionItem::getCommission).sum();
                employeecommission.setText("" + totalCommission);


            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }


    public void setEnumber(BigInteger enumber) {
        searchfield.setText(enumber.toString());
    }


}
