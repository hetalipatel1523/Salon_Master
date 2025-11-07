package org.salonmaster.salonmaster.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeregisterController implements Initializable {

    @FXML
    private TextField Eaddress;

    @FXML
    private TextField Ecomission;

    @FXML
    private TextField Eemail;

    @FXML
    private TextField Ename;

    @FXML
    private TextField Enumber;

    private boolean update;

    @FXML
    private Button Submit;
    private int Id;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private PreparedStatement pstmt;

    private Connection con = Connectionprovider.getConnection();

    @FXML
    public void Back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Submit(ActionEvent event) throws IOException, SQLException {
        String name = Ename.getText();
        String phoneNumberText = Enumber.getText();
        String email = Eemail.getText();
        String commissionText = Ecomission.getText();
        String address = Eaddress.getText();

        if (name.isEmpty() || phoneNumberText.isEmpty() || email.isEmpty() || commissionText.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all details!");
            return;
        }


        BigInteger phoneNumber = new BigInteger(phoneNumberText);
        int commission = Integer.parseInt(commissionText);

        if (!update) {
            String insertsql = "INSERT INTO ms_ereg (ename, eaddress, eemail, enumber, commission) VALUES (?, ?, ?, ?, ?)"
                    + " ON CONFLICT ON CONSTRAINT uniq_pho_constraint DO NOTHING;";

            try (Connection con = Connectionprovider.getConnection(); PreparedStatement pstmt = con.prepareStatement(insertsql)) {

                pstmt.setString(1, name);
                pstmt.setString(2, address);
                pstmt.setString(3, email);
                pstmt.setObject(4, phoneNumber);
                pstmt.setInt(5, commission);

                int data = pstmt.executeUpdate();

                if (data > 0) {
                    ClearFields();
                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Employee registration successful.");
                } else {
                    JOptionPane.showMessageDialog(null, "Employee already exists");
                }
            }
        } else {
            String sql = "UPDATE ms_ereg SET ename=?, eaddress=?, eemail=?, commission=?, enumber=? WHERE id=?";

            try (Connection con = Connectionprovider.getConnection(); PreparedStatement statement = con.prepareStatement(sql)) {

                statement.setString(1, name);
                statement.setString(2, address);
                statement.setString(3, email);
                statement.setInt(4, commission);
                statement.setObject(5, phoneNumber);
                statement.setInt(6, Id);

                int data = statement.executeUpdate();

                if (data > 0) {
                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/EmployeeList.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Employee details updated successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Update failed");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle the exception appropriately, e.g., show error message
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Method to check if email exists in the database
    private boolean emailExists(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM ms_ereg WHERE eemail = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any errors here
        }
        return false;
    }

    // Method to validate email format using regular expression
    private boolean isValidEmail(String email) {
        // Regular expression for basic email validation
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

//    public void setEmployeeData(Employeelist employee) {
//        Ename.setText(employee.getEname());
//        Enumber.setText(employee.getEnumber().toString());
//        Eemail.setText(employee.getEemail());
//        Ecomission.setText(employee.getComission().toString());
//        Eaddress.setText(employee.getEaddress());
//    }
    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void ClearFields() {
        Ename.clear();
        Enumber.clear();
        Ecomission.clear();
        Eaddress.clear();
        Eemail.clear();
    }

    void setTextField(int Id, String name, BigInteger phone, String email, Integer commission, String address) {
        this.Id = Id;
        Ename.setText(name);
        Enumber.setText(phone.toString());
        Eemail.setText(email);
        Ecomission.setText(commission.toString());
        Eaddress.setText(address);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }
}
