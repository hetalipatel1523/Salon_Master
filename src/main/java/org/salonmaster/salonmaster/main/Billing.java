package org.salonmaster.salonmaster.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Billing extends Application {

    @Override   
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Salon Master");      
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
//Radhe Radhe 