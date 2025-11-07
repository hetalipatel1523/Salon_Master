package org.salonmaster.salonmaster.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FXML Controller class
 *
 * @author Ayush
 */
public class AdminLoginController implements Initializable {

    @FXML
    private Label titlelab;
    @FXML
    private Label Adminlab;
    @FXML
    private TextField UnameTextField;

    @FXML
    private PasswordField myPasswordField;
    @FXML
    private Button loginbt;

    private PreparedStatement pst;
    private ResultSet rs;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection con;

    @FXML
    public void submit(ActionEvent event) throws IOException {
        String Uname = UnameTextField.getText();
        String Pass = myPasswordField.getText();

        if (Uname.isEmpty() || Pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username or password blank");
        } else {
            try {
                con = Connectionprovider.getConnection();

                pst = con.prepareStatement("select * from ms_adminuser where username=? and password=?");

                pst.setString(1, Uname);
                pst.setString(2, Pass);

                rs = pst.executeQuery();

                if (rs.next()) {

//                    session.setUsername(Uname);
                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/admindesk.fxml"));

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Login Success");
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed");
                    UnameTextField.setText("");
                    myPasswordField.setText("");
                    UnameTextField.requestFocus();
                }

            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

    }
}