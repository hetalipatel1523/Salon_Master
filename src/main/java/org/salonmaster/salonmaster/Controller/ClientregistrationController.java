package org.salonmaster.salonmaster.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientregistrationController implements Initializable {

    @FXML
    private TextField C_name;

    @FXML
    private TextField c_email;

    @FXML
    private TextField c_gst;

    @FXML
    private PasswordField c_password;

    @FXML
    private TextField c_phone;

    @FXML
    private TextField c_salon;

    @FXML
    private TextArea c_address;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean update;

    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/admindesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
public void submit(ActionEvent event) throws IOException {
    String Clientname = C_name.getText();
    String Salonname = c_salon.getText();
    String Emailid = c_email.getText();
    String Address = c_address.getText();
    String Phonenumber = c_phone.getText();
    String GSTnumber = c_gst.getText();
    String Password = c_password.getText();

    String emailreg = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+";
    String gstreg = "^\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}\\d[Z]{1}[A-Z\\d]{1}$";

    if (Clientname.isEmpty() || Salonname.isEmpty() || Emailid.isEmpty() || Phonenumber.isEmpty() || GSTnumber.isEmpty() || Password.isEmpty() || Address.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Fill all details");
    } else if (!Emailid.matches(emailreg)) {
        JOptionPane.showMessageDialog(null, "Invalid Email");
    } else if (!Phonenumber.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(null, "Invalid Phone Number");
    } else if (Password.contains(" ")) {
        JOptionPane.showMessageDialog(null, "Password cannot contain whitespace");
    } else if (!GSTnumber.matches(gstreg)) {
        JOptionPane.showMessageDialog(null, "Invalid GST number");
    } else {
        try (Connection con = Connectionprovider.getConnection()) {
            String query;

            if (!update) {
                query = "INSERT INTO ms_creg(c_name, s_name, phone_no, email, gst, password, address) VALUES (?, ?, ?, ?, ?, ?, ?)"
                        + " ON CONFLICT ON CONSTRAINT uniq_cust_constraint DO NOTHING;";
            } else {
                query = "UPDATE ms_creg SET "
                        + "c_name=?,"
                        + "s_name=?,"
                        + "phone_no=?,"
                        + "email=?,"
                        + "gst=?,"
                        + "password=?,"
                        + "address=? WHERE email = ?";
            }

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, Clientname);
                pstmt.setString(2, Salonname);
                pstmt.setString(3, Phonenumber);
                pstmt.setString(4, Emailid);
                pstmt.setString(5, GSTnumber);
                pstmt.setString(6, Password);
                pstmt.setString(7, Address);

                if (update) {
                    pstmt.setString(8, Emailid);
                }

                int data = pstmt.executeUpdate();

                if (!update) {
                    if (data > 0) {
                        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/admindesk.fxml"));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        JOptionPane.showMessageDialog(null, "Client registered successfully");

                        C_name.setText("");
                        c_salon.setText("");
                        c_email.setText("");
                        c_address.setText("");
                        c_phone.setText("");
                        c_gst.setText("");
                        c_password.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Client already exists");
                    }
                } else {
                    if (data > 0) {
                        JOptionPane.showMessageDialog(null, "Client details updated successfully");
                        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/admindesk.fxml"));
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed");
                    }
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}


    void setTextField(String Clientname, String Salonname, String Emailid, String Phonenumber, String GSTnumber, String password, String Address) {
        C_name.setText(Clientname);
        c_salon.setText(Salonname);
        c_email.setText(Emailid);
        c_phone.setText(Phonenumber);
        c_gst.setText(GSTnumber);
        c_password.setText(password);
        c_address.setText(Address);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
