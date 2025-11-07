package org.salonmaster.salonmaster.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLogin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AdminLogin.class.getResource("/org/salonmaster/salonmaster/FXML/AdminLogin.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("AdminLogin");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}