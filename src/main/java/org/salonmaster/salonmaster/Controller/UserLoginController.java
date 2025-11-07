package org.salonmaster.salonmaster.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserLoginController {


    @FXML
    private Button mybutton;


    @FXML
    private Button Showpassword;


    @FXML
    private Button Hidepassword;


    @FXML
    private TextField usernameField;


    @FXML
    private PasswordField passwordField;


    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection con;
    private boolean passwordVisible = false; // Flag to track password visibility




    @FXML
    private void submit(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();


        try {
            con = Connectionprovider.getConnection();
            String query = "SELECT * FROM ms_creg WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {


                root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                showAlert("Login Successful", "Welcome " + username);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


//    @FXML
//    private void togglePasswordVisibility(ActionEvent event) {
//        if (passwordField.isVisible()) {
//            // Switch to show password
//            passwordField.setVisible(false);
//            TextField textField = new TextField(passwordField.getText());
//            textField.setPromptText("Password");
//            textField.setPrefWidth(passwordField.getWidth());
//            textField.setPrefHeight(passwordField.getHeight());
//            textField.setFont(passwordField.getFont());
//            textField.setId("temporaryTextField");
//            textField.setLayoutX(passwordField.getLayoutX());
//            textField.setLayoutY(passwordField.getLayoutY());
//            ((Pane) passwordField.getParent()).getChildren().add(textField);
//            Showpassword.setText("Show Password");
////            Hidepassword.setText("Hide Password"); // Set the text for the Hidepassword button
//        } else {
//            // Switch to hide password
//            TextField textField = (TextField) ((Pane) passwordField.getParent()).lookup("#temporaryTextField");
//            passwordField.setText(textField.getText());
//            ((Pane) passwordField.getParent()).getChildren().remove(textField);
//            passwordField.setVisible(true);
////            Showpassword.setText("Hide Password");
//            Hidepassword.setText("Hide Password"); // Reset the text for the Hidepassword button
//        }
//    }

//
//    @FXML
//    private void hidePasswordField(ActionEvent event) {
//        TextField textField = (TextField) ((Pane) passwordField.getParent()).lookup("#temporaryTextField");
//        ((Pane) passwordField.getParent()).getChildren().remove(textField);
//        passwordField.setVisible(true);
//        Showpassword.setText("Show Password");
//        Hidepassword.setText("Hide Password"); // Reset the text for the Hidepassword button
//    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
