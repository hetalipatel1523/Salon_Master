package org.salonmaster.salonmaster.Controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.BillingItem;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingController implements Initializable {

    private final ObservableList<String> options = FXCollections.observableArrayList();
    private final ObservableList<BillingItem> billingItemList = FXCollections.observableArrayList();
    private int invoiceNumber = 1;
    private String employeeNumber;

    @FXML
    private TextField productsearch;

    @FXML
    private TextField employeename;

    @FXML
    private ComboBox<String> payment;

    @FXML
    private Label Amount;

    @FXML
    private Label commission;

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private TextField namet;

    @FXML
    private TextField phonenot;

    @FXML
    private TableView<BillingItem> Billing;

    @FXML
    private TableColumn<BillingItem, Integer> s_id;

    @FXML
    private TableColumn<BillingItem, String> t_item;

    @FXML
    private TableColumn<BillingItem, Integer> t_price;

    @FXML
    private TableColumn<BillingItem, String> t_stock;

    @FXML
    private TableColumn<BillingItem, String> t_category;

    @FXML
    private TableColumn<BillingItem, String> t_ps;

    @FXML
    private Label label;

    @FXML
    private Label tax;

    @FXML
    private Label total;

    @FXML
    private Label reward;

    @FXML
    private Label invoiceno;

    @FXML
    private Label aptime;

    @FXML
    private Button printsave;

    @FXML
    private DatePicker date;

    @FXML
    private Label time;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane navSlider;

    @FXML
    private ScrollPane scroll;

    @FXML
    private StackPane Slider;

    @FXML
    private TextField id, item, stock, price, category, ps;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection con = Connectionprovider.getConnection();
    private ResultSet rs;
    private PreparedStatement pst;
    private boolean uupdate;

    String postfix = "";
    String prefix = "";

    @FXML
    public void exit(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Billing");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setTranslateX(-800);
        menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(scroll);

            slide.setToX(0);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(scroll);

            slide.setToX(-800);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });

        Slider.setTranslateY(600);
        Billing.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(Slider);
            slide.setToY(0);
            slide.play();

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.4), Slider);
                slideOut.setToY(600);
                slideOut.play();
            });
            pause.play();
        });

        s_id.setReorderable(false);
        t_item.setReorderable(false);
        t_price.setReorderable(false);
        t_stock.setReorderable(false);
        t_category.setReorderable(false);
        t_ps.setReorderable(false);

        productsearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String Query = "select id,item,price,stock,category,ps from ms_im where item = ?";
                pst = con.prepareStatement(Query);
                pst.setString(1, newValue);
                rs = pst.executeQuery();

                while (rs.next()) {
                    item.setText(rs.getString("item"));
                    int stockValue = rs.getInt("stock");
                    int priceValue = rs.getInt("price");
                    price.setText(String.valueOf(priceValue));
                    stock.setText("1");
                    category.setText(rs.getString("category"));
                    ps.setText(rs.getString("ps"));
                }

            } catch (SQLException ex) {
                Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        employeename.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String commissionQuery = "SELECT commission FROM ms_ereg WHERE ename = ?";
                    pst = con.prepareStatement(commissionQuery);
                    pst.setString(1, newValue);
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        double commissionAmount = rs.getDouble("commission");
                        commission.setText("Commission: " + commissionAmount); // Set commission amount to the label
                    } else {
                        commission.setText("Commission: N/A"); // If no commission found for the employee, display N/A
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Other initialization code...
        // Auto-binding code for employee names
        try {
            String employeeQuery = "SELECT ename FROM ms_ereg";
            pst = con.prepareStatement(employeeQuery);
            rs = pst.executeQuery();

            ObservableList<String> employeeNames = FXCollections.observableArrayList();
            while (rs.next()) {
                employeeNames.add(rs.getString("ename"));
            }

//            TextFields.bindAutoCompletion(employeename, employeeNames);

        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        employeename.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String getEmployeeNumberQuery = "SELECT enumber FROM ms_ereg WHERE ename = ?";
                    pst = con.prepareStatement(getEmployeeNumberQuery);
                    pst.setString(1, newValue);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        employeeNumber = rs.getString("enumber");
                    }
                } catch (SQLException ex) {
                    System.err.println("SQL Error: " + ex.getMessage());
                    ex.printStackTrace(); // Print stack trace for detailed error information
                }
            }
        });

//        try {
//            String employeeQuery = "SELECT ename FROM ms_ereg";
//            pst = con.prepareStatement(employeeQuery);
//            rs = pst.executeQuery();
//
//            ObservableList<String> employeeNames = FXCollections.observableArrayList();
//            while (rs.next()) {
//                employeeNames.add(rs.getString("ename"));
//            }
//
//            TextFields.bindAutoCompletion(employeename, employeeNames);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        phonenot.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String query = "SELECT name FROM ms_user WHERE phone_no = ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, newValue);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        namet.setText(rs.getString("name"));
                    }

                    fetchAppointmentDetails(newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        FillComboBox();

        payment.setItems(FXCollections.observableArrayList("CASH", "UPI", "CARD"));

        try {
            String query = "select name from ms_user";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            ObservableList<String> names = FXCollections.observableArrayList();

            while (rs.next()) {
                names.add(rs.getString("name"));
            }
//            TextFields.bindAutoCompletion(namet, names);
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String query = "select phone_no from ms_user";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            ObservableList<String> phones = FXCollections.observableArrayList();

            while (rs.next()) {
                phones.add(rs.getString("phone_no"));
            }
//            TextFields.bindAutoCompletion(phonenot, phones);
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String query = "select item from ms_im";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            ObservableList<String> products = FXCollections.observableArrayList();

            while (rs.next()) {
                products.add(rs.getString("item"));
            }

//            TextFields.bindAutoCompletion(productsearch, products);
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        t_item.setCellValueFactory(new PropertyValueFactory<>("item"));
        t_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        t_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        t_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        t_ps.setCellValueFactory(new PropertyValueFactory<>("ps"));
        s_id.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        try {
            String query = "SELECT MAX(invoice_no) AS last_invoice FROM ms_bl";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                String lastInvoiceNumberString = rs.getString("last_invoice");
                int lastInvoiceNumber = Integer.parseInt(lastInvoiceNumberString);
                invoiceNumber = lastInvoiceNumber + 1;
                invoiceno.setText("" + invoiceNumber);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            con = Connectionprovider.getConnection();
            String query = "SELECT postfix , prefix FROM ms_setting;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                postfix = rs.getString("postfix");
                prefix = rs.getString("prefix");
            }

            invoiceno.setText("Invoice No:" + postfix + invoiceNumber + prefix);

        } catch (SQLException e) {
            System.out.println(e);
        }
        date.setValue(LocalDate.now());

        employeename.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String comissionQuery = "SELECT commission, enumber FROM ms_ereg WHERE ename = ?";
                    pst = con.prepareStatement(comissionQuery);
                    pst.setString(1, newValue);
                    rs = pst.executeQuery();

                } catch (SQLException ex) {
                    Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void fetchAppointmentDetails(String phoneNumber) {
        try {
            String appointmentQuery = "SELECT date,time FROM ms_apb WHERE user_id IN (SELECT id FROM ms_user WHERE phone_no = ?)";
            pst = con.prepareStatement(appointmentQuery);
            pst.setString(1, phoneNumber);
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FillComboBox() {
        try {
            String query = "select item from ms_im";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                options.add(rs.getString("item"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void delete(ActionEvent event) {
        BillingItem selectedItem = Billing.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            billingItemList.remove(selectedItem);
            Billing.setItems(billingItemList);
            updateTotal();
        }
    }

    @FXML
    public void add(ActionEvent event) {
        String categoryText = category.getText();
        String itemText = item.getText();

        if (price.getText().isEmpty()) {
            showAlert("Error", "Please enter a price for the item.");
            return;
        }

        int priceValue = Integer.parseInt(price.getText());
        String qtyText = stock.getText();
        String psText = ps.getText();

        int newSerialNumber = billingItemList.size() + 1;

        BillingItem newItem = new BillingItem(newSerialNumber, itemText, priceValue, qtyText, categoryText, psText);

        billingItemList.add(newItem);
        Billing.setItems(billingItemList);

        updateTotal();
        clearFields();
    }

    @FXML
    private void updateTotal() {
        double totalPrice = 0;
        double totalAmountWithoutGST = 0;

        for (BillingItem item : billingItemList) {
            double price = item.getPrice();
            int quantity = Integer.parseInt(item.getStock());
            totalPrice += price * quantity;
            totalAmountWithoutGST += price * quantity;
        }

        double gst = 0.18 * totalPrice;
        double totalAmount = totalAmountWithoutGST + gst;
//        int rewardPoints = (int) totalAmountWithoutGST;

//     Setting module 
        float rewardPoints = 0.0f;
        try {
            con = Connectionprovider.getConnection();
            String query = "SELECT amount FROM ms_setting ORDER BY id DESC LIMIT 1;";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                rewardPoints = rs.getFloat("amount");
            }
            float totalreward = (float) (totalAmount * rewardPoints);

            total.setText("Total :   " + String.format("%.2f", totalAmount));
            tax.setText("GST (18%) :   " + String.format("%.2f", gst));
            Amount.setText("Amount :   " + String.format("%.2f", totalAmountWithoutGST));
            reward.setText("Reward Points: " + totalreward);
        } catch (SQLException e) {
            System.out.println(e);
        }
//    ---------------------------------------------
    }

    private void decreaseInventory() {
        try {
            con = Connectionprovider.getConnection();

            String getStatusQuery = "SELECT ns_status FROM ms_setting ORDER BY id DESC LIMIT 1;";
            PreparedStatement getStatusStmt = con.prepareStatement(getStatusQuery);
            ResultSet statusResult = getStatusStmt.executeQuery();

            int nsStatus = 0;
            if (statusResult.next()) {
                nsStatus = statusResult.getInt("ns_status");
            }

            for (BillingItem item : billingItemList) {
                String itemText = item.getItem();
                String qtyText = item.getStock();

                if (!qtyText.isEmpty()) {
                    int qty = Integer.parseInt(qtyText);

                    try {
                        String updateQuery = "";
                        if (nsStatus == 1 || qty == 0) {
                            updateQuery = "UPDATE ms_im SET stock = stock - ? WHERE item = ? AND ps = 'Product';";
                        } else {
                            showAlert("error", "Stock is not available for item: " + itemText + " = " + qtyText);
                        }
                        pst = con.prepareStatement(updateQuery);
                        pst.setInt(1, qty);
                        pst.setString(2, itemText);
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @FXML
    public void printSave(ActionEvent event) {
        if (namet.getText().isEmpty() || phonenot.getText().isEmpty() || employeename.getText().isEmpty()) {
            showAlert("Error", "Fill all details.");
            return;
        }

        if (payment.getSelectionModel().isEmpty()) {
            showAlert("Error", "Please select a payment method.");
            return;
        }
        if (Billing.getItems().isEmpty()) {
            showAlert("Error", "Add Item to the table");
            return;
        }
        try {
            double commissionPercentage = 0;
            if (!commission.getText().isEmpty()) {
                String commissionValue = commission.getText().replace("Commission: ", "");
                commissionPercentage = Double.parseDouble(commissionValue);
            }
            double commissionAmount = Double.parseDouble(Amount.getText().replace("Amount :   ", "")) * (commissionPercentage / 100);
            // Get the employee number corresponding to the selected employee name
            String selectedEmployeeName = employeename.getText();
            String getEmployeeNumberQuery = "SELECT enumber FROM ms_ereg WHERE ename = ?";
            pst = con.prepareStatement(getEmployeeNumberQuery);
            pst.setString(1, selectedEmployeeName);
            rs = pst.executeQuery();
            if (rs.next()) {
                employeeNumber = rs.getString("enumber");
            } else {
                System.err.println("No employee number found for the selected employee name: " + selectedEmployeeName);
                return;
            }
            for (BillingItem item : billingItemList) {
                String customerPhoneNumber = phonenot.getText();
                String employeeName = employeename.getText();
                String insertQuery = "INSERT INTO ms_bl (item, price, stock, category, payment_method, date, invoice_no, reward, custnum, ps, ename, commission, enumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = con.prepareStatement(insertQuery);
                pst.setString(1, item.getItem());
                pst.setInt(2, item.getPrice());
                pst.setString(3, item.getStock());
                pst.setString(4, item.getCategory());
                pst.setString(5, payment.getValue());
                pst.setDate(6, Date.valueOf(date.getValue()));
                pst.setInt(7, invoiceNumber);
                pst.setFloat(8, Float.parseFloat(reward.getText().split(":")[1].trim()));
                pst.setString(9, customerPhoneNumber);
                pst.setString(10, item.getPs());
                pst.setString(11, employeeName);
                pst.setDouble(12, commissionAmount);
                pst.setString(13, employeeNumber);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Bill Saved");
                String insertRewardQuery = "INSERT INTO ms_rew (number, reward) VALUES (?, ?) ON CONFLICT (number) DO UPDATE SET reward = ms_rew.reward + EXCLUDED.reward";
                pst = con.prepareStatement(insertRewardQuery);
                pst.setString(1, customerPhoneNumber);
                pst.setFloat(2, Float.parseFloat(reward.getText().split(":")[1].trim()));
                pst.executeUpdate();
            }
            decreaseInventory();
            invoiceNumber++;
            clearnamephone();

        } catch (SQLException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
            invoiceno.setText("Invoice No:" + postfix + invoiceNumber + prefix);
        billingItemList.clear();
        Billing.setItems(billingItemList);
    }
    private void clearFields() {
        item.clear();
        stock.clear();
        category.clear();
        ps.clear();
        productsearch.clear();
        price.clear();
    }

    private void clearnamephone() {
        namet.clear();
        phonenot.clear();
        employeename.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Alert");
        alert.setContentText(content);
        alert.showAndWait();
    }

    void setTextField(String name, String phone_no) {
        namet.setText(name);
        phonenot.setText(phone_no);
    }
    void setUpdate(boolean b) {
        this.uupdate = b;
    }

//    Redirect pages
    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Setting(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Setting.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void apb_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/APPOINMENTS.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_reg(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerregistration.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/CUSTOMERLIST.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void add_product(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Inventory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Im(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Product.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Report(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Report.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Reward(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Reward.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void emp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void blhistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billinghistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
