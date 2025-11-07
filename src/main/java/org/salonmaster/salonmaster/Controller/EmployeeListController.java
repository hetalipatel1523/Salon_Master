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
import org.salonmaster.salonmaster.GETSET.Employeelist;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeListController implements Initializable {

    @FXML
    private Button EDIT;

    @FXML
    private TableView<Employeelist> employeelisttt;

    @FXML
    private TableColumn<Employeelist, String> Eaddress;

    @FXML
    private TableColumn<Employeelist, Integer> Ecomission;

    @FXML
    private TableColumn<Employeelist, String> Eemail;

    @FXML
    private TableColumn<Employeelist, String> Ename;

    @FXML
    private TableColumn<Employeelist, BigInteger> Enumber;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane Slider;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Employeelist employeelist;

    private Connection con = Connectionprovider.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Slider.setTranslateY(600);
        employeelisttt.setOnMouseClicked(event -> {
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

        Ename.setReorderable(false);
        Enumber.setReorderable(false);
        Eemail.setReorderable(false);
        Ecomission.setReorderable(false);
        Eaddress.setReorderable(false);

        // Set cell value factories for table columns
        Ename.setCellValueFactory(new PropertyValueFactory<>("ename"));
        Enumber.setCellValueFactory(new PropertyValueFactory<>("enumber"));
        Eemail.setCellValueFactory(new PropertyValueFactory<>("eemail"));
        Ecomission.setCellValueFactory(new PropertyValueFactory<>("commission"));
        Eaddress.setCellValueFactory(new PropertyValueFactory<>("eaddress"));

        // Fetch data from the database and populate TableView
        loadDataFromDatabase();

    }

    @FXML
    public void delete(ActionEvent event) {
        Employeelist selectedItem = employeelisttt.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete Employee");
            alert.setContentText("Are you sure you want to delete this employee?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If user confirms deletion
                try {
                    // Remove the selected item from the TableView
                    employeelisttt.getItems().remove(selectedItem);
                    // Delete the item from the database
                    deleteFromDatabase(selectedItem);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle any database errors
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Database Error");
                    errorAlert.setContentText("An error occurred while deleting the employee from the database.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // If no item is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    public void edit(ActionEvent event) {
        Employeelist employeelist = employeelisttt.getSelectionModel().getSelectedItem();
        if (employeelist == null) {
            Showalert();
        }

        Stage primaryStage = (Stage) employeelisttt.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/salonmaster/salonmaster/FXML/Employeeregister.fxml")); // Path to Inventory.fxml
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }

        EmployeeregisterController employeeregisterController = loader.getController(); // Use InventoryController class
        employeeregisterController.setUpdate(true);
        employeeregisterController.setTextField(
                employeelist.getId(),
                employeelist.getEname(),
                employeelist.getEnumber(),
                employeelist.getEemail(),
                employeelist.getCommission(),
                employeelist.getEaddress()
        );
        scene = new Scene(root); // Declare scene here
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void commision(ActionEvent event) throws IOException {
        // Get the selected employee
        Employeelist selectedEmployee = employeelisttt.getSelectionModel().getSelectedItem();
        Stage primaryStage = (Stage) employeelisttt.getScene().getWindow();

        if (selectedEmployee != null) {
            // Get the Enumber of the selected employee
            BigInteger enumber = selectedEmployee.getEnumber();

            // Load the commissiontable FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/salonmaster/salonmaster/FXML/Commissiontable.fxml"));
            Parent root = loader.load();

            // Access the controller of commissiontable
            Commissiontable_controller commissionTableController = loader.getController();

            // Pass the Enumber to the commissiontable controller
            commissionTableController.setEnumber(enumber);

            // Show the commissiontable stage
            scene = new Scene(root); // Declare scene here
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            // If no employee is selected, show a warning message
            ShowAlert("No Employee Selected", "Please select an employee to add commission.");
        }
    }

// Utility method to show alerts
    private void ShowAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void deleteFromDatabase(Employeelist employee) throws SQLException {
        String query = "DELETE FROM ms_ereg WHERE enumber = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setBigDecimal(1, new BigDecimal(employee.getEnumber()));
        preparedStatement.executeUpdate();
    }

    private void loadDataFromDatabase() {
        try {
            ObservableList<Employeelist> employeeList = FXCollections.observableArrayList();
            String query = "SELECT * FROM ms_ereg";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Retrieve data from the ResultSet
                int id = resultSet.getInt("id");
                String ename = resultSet.getString("ename");
                BigInteger enumber = resultSet.getBigDecimal("enumber").toBigInteger();
                String eemail = resultSet.getString("eemail");
                Integer comission = resultSet.getInt("commission");
                String eaddress = resultSet.getString("eaddress");

                // Create Employeelist objects and add them to the ObservableList
                Employeelist employee = new Employeelist(id, ename, enumber, eemail, comission, eaddress);
                employeeList.add(employee);
            }
            // Populate TableView with the data
            employeelisttt.setItems(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Showalert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Value Entered");
        alert.setHeaderText(null);
        alert.setContentText("Please select data from  table.");
        alert.showAndWait();
    }

    @FXML
    public void add(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Employeeregister.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
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
    public void booking(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/BookAppointment.fxml"));
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
