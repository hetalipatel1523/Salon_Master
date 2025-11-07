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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.BillEntry;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BillinghistoryController implements Initializable {

    @FXML
    public TextField searchbar;

    @FXML
    private Button search;

    @FXML
    private TableView<BillEntry> Billinghistory;

    @FXML
    private TableColumn<BillEntry, Integer> invoiceNoColumn;

    @FXML
    private TableColumn<BillEntry, String> custNumColumn;

    @FXML
    private TableColumn<BillEntry, String> itemColumn;

    @FXML
    private TableColumn<BillEntry, String> paymentMethodColumn;

    @FXML
    private TableColumn<BillEntry, String> dateColumn;

    @FXML
    private TableColumn<BillEntry, String> stockColumn;

    @FXML
    private TableColumn<BillEntry, Integer> priceColumn;

    private Connection con;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private Text month;

    @FXML
    private StackPane navSlider;

    @FXML
    private StackPane Slider;

    @FXML
    private Button EDIT;

    @FXML
    private Button DELETE;

    @FXML
    private ScrollPane scroll;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<BillEntry> billEntries = FXCollections.observableArrayList();

    @FXML
    public void delete(ActionEvent event) {
        BillEntry selectedEntry = Billinghistory.getSelectionModel().getSelectedItem();

        if (selectedEntry == null) {
            showAlert(Alert.AlertType.ERROR, "No Item Selected", "Please select an entry to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Entry");
        alert.setContentText("Are you sure you want to delete this entry?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String query = "DELETE FROM ms_bl WHERE invoice_no = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, selectedEntry.getInvoiceNo());

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        billEntries.remove(selectedEntry);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the entry.");
                    }

                    preparedStatement.close();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the entry.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void edit(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Slider.setTranslateY(600);
        Billinghistory.setOnMouseClicked(event -> {
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

        //        Search bar and date bar
        search.setOnMouseClicked(event -> {
            searchbar.setVisible(!searchbar.isVisible());
        });

        con = Connectionprovider.getConnection();

        if (con == null) {
            System.err.println("Error: Unable to establish connection to the database.");
            return;
        }

        initializeColumns();
        fetchDataFromDatabase();
        setupSearchFilter();
    }

    private void initializeColumns() {
        invoiceNoColumn.setCellValueFactory(cellData -> cellData.getValue().invoiceNoProperty().asObject());
        custNumColumn.setCellValueFactory(cellData -> cellData.getValue().custNumProperty());
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().itemProperty());
        paymentMethodColumn.setCellValueFactory(cellData -> cellData.getValue().paymentMethodProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }

    private void fetchDataFromDatabase() {
        try {
            String query = "SELECT invoice_no, custnum, item, payment_method, date, stock, price FROM ms_bl";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int invoiceNo = resultSet.getInt("invoice_no");
                String custNum = resultSet.getString("custnum");
                String item = resultSet.getString("item");
                String paymentMethod = resultSet.getString("payment_method");
                String date = resultSet.getString("date");
                String stock = resultSet.getString("stock");
                int price = resultSet.getInt("price");

                billEntries.add(new BillEntry(invoiceNo, custNum, item, paymentMethod, date, stock, price));
            }

            Billinghistory.setItems(billEntries);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching data from the database: " + e.getMessage());
            System.out.println(e);
        }
    }

    private void setupSearchFilter() {
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                Billinghistory.setItems(billEntries);
                return;
            }

            ObservableList<BillEntry> filteredList = FXCollections.observableArrayList();

            for (BillEntry entry : billEntries) {
                if (entry.getCustNum().contains(newValue)) {
                    filteredList.add(entry);
                }
            }

            Billinghistory.setItems(filteredList);
        });
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
    public void Setting(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Setting.fxml"));
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
    public void apb_list(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/APPOINMENTS.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
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
    public void emp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
