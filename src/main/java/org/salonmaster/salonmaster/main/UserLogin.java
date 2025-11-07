package org.salonmaster.salonmaster.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserLogin extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/UserLogin.fxml"));

        Scene scene = new Scene(root);

        TextField textfield = new TextField();
        textfield.getStyleClass().add("Username");
        stage.setTitle("Salon Master");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
