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
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BookAppointmentController implements Initializable {

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    @FXML
    private TextField NAME;
    @FXML
    private TextField PHONE;
    @FXML
    private DatePicker DATE;
    @FXML
    private TextField searchappointment;
    @FXML
    private Button APPBOOKING;
    @FXML
    private Label userNotFoundLabel;
    @FXML
    private ComboBox<String> hours;
    @FXML
    private ComboBox<String> minute;
    @FXML
    private ComboBox<String> AP;
    @FXML
    private Button REGBOOK;
    @FXML
    private Button menu;

    private static final String DATE_REGEX = "^\\d{1,2}-\\d{1,2}-\\d{4}$";
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void APPBOOKING(ActionEvent event) {
        String name = NAME.getText();
        String phoneNo = PHONE.getText();
        LocalDate selectedDate = DATE.getValue();
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();

        // Check if any field is empty
        if (name.isEmpty() || phoneNo.isEmpty() || selectedDate == null || selectedHour == null || selectedMinute == null || selectedAMPM == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields must be required");
            return;
        }

        // Validate phone number format
        if (!phoneNo.matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Phone Number");
            return;
        }

        // Validate date format
        if (!selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).matches(DATE_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Use this Date format: dd-mm-yyyy");
            return;
        }

        // Proceed to book the appointment
        con = Connectionprovider.getConnection();
        String checkPhoneQuery = "SELECT id FROM ms_user WHERE phone_no=?";

        try (PreparedStatement checkStmt = con.prepareStatement(checkPhoneQuery)) {
            checkStmt.setString(1, phoneNo);
            ResultSet userResultSet = checkStmt.executeQuery();

            if (userResultSet.next()) {
                int userId = userResultSet.getInt("id");
                String time = formatTime();

                String insertAppointmentQuery = "INSERT INTO ms_apb (user_id, date, time) VALUES (?, ?, ?)";
                try (PreparedStatement apbStatement = con.prepareStatement(insertAppointmentQuery)) {
                    apbStatement.setInt(1, userId);
                    apbStatement.setDate(2, java.sql.Date.valueOf(selectedDate));
                    apbStatement.setString(3, time);

                    apbStatement.executeUpdate();

                    NAME.clear();
                    PHONE.clear();
                    DATE.setValue(null);

                    navigateTo(event, "/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml");
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment booked successfully!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Error", "User not registered. Please register first.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void REGBOOK(ActionEvent event) {
        String name = NAME.getText();
        String phoneNo = PHONE.getText();
        LocalDate selectedDate = DATE.getValue();
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();

        if (name.isEmpty() || phoneNo.isEmpty() || selectedDate == null || selectedHour == null || selectedMinute == null || selectedAMPM == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields must be required");
            return;
        }

        if (!phoneNo.matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Phone Number");
            return;
        }

        if (!selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).matches(DATE_REGEX)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Use this Date format: dd-mm-yyyy");
            return;
        }

        String checkPhoneQuery = "SELECT id FROM ms_user WHERE phone_no=?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkPhoneQuery)) {
            checkStmt.setString(1, phoneNo);
            ResultSet userResultSet = checkStmt.executeQuery();

            if (userResultSet.next()) {
                showAlert(Alert.AlertType.WARNING, "Error", "Customer already exists");
            } else {
                try (PreparedStatement userStmt = con.prepareStatement("INSERT INTO ms_user (name, phone_no) VALUES (?, ?) RETURNING id")) {
                    userStmt.setString(1, name);
                    userStmt.setString(2, phoneNo);
                    try (ResultSet userRs = userStmt.executeQuery()) {
                        if (userRs.next()) {
                            int userId = userRs.getInt(1);
                            String time = formatTime();

                            try (PreparedStatement abStmt = con.prepareStatement("BEGIN; INSERT INTO ms_cureg (userid) VALUES (?); INSERT INTO ms_apb (user_id, date, time) VALUES (?, ?, ?); COMMIT;")) {
                                abStmt.setInt(1, userId);
                                abStmt.setInt(2, userId);
                                abStmt.setDate(3, java.sql.Date.valueOf(selectedDate));
                                abStmt.setString(4, time);

                                abStmt.executeUpdate();
                                navigateTo(event, "/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml");
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Registered and Booked successfully!");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatTime() {
        String selectedHour = hours.getValue();
        String selectedMinute = minute.getValue();
        String selectedAMPM = AP.getValue();
        return selectedHour + ":" + selectedMinute + " " + selectedAMPM;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchappointment.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    pstmt = con.prepareStatement("SELECT name, phone_no FROM ms_user WHERE phone_no = ?");
                    pstmt.setString(1, newValue);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        NAME.setText(rs.getString("name"));
                        PHONE.setText(rs.getString("phone_no"));
                        userNotFoundLabel.setVisible(false);
                    } else {
                        NAME.clear();
                        PHONE.clear();
                        userNotFoundLabel.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                NAME.clear();
                PHONE.clear();
                userNotFoundLabel.setVisible(false);
            }
        });

        populateSearchAndComboBox();
    }

    private void populateSearchAndComboBox() {
        try {
            con = Connectionprovider.getConnection();
            ObservableList<String> searchList = FXCollections.observableArrayList();

            pstmt = con.prepareStatement("SELECT phone_no FROM ms_user;");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                searchList.add(rs.getString("phone_no"));
            }
//            TextFields.bindAutoCompletion(searchappointment, searchList);

            DATE.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(LocalDate.now()));
                }
            });

            AP.setItems(FXCollections.observableArrayList("AM", "PM"));
            hours.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
            minute.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        navigateTo(event, "/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
