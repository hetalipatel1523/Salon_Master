package org.salonmaster.salonmaster.Controller;

import javafx.animation.ParallelTransition;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    private Button BACK;

    @FXML
    private DatePicker DATE;

    @FXML
    private TextField NAME;

    @FXML
    private TextField PHONE;

    @FXML
    private ComboBox<String> AP;

    @FXML
    private ComboBox<String> hours;

    @FXML
    private ComboBox<String> minute;

    @FXML
    private Button updat;

    @FXML
    private StackPane Slider;
    
    @FXML
    private ScrollPane scroll;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    private boolean update;
    private int Id;
    private Connection con;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void back(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/BookAppointment.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    // Concatenate the selected hour, minute, and AM/PM to form the time string
    @FXML
    public void Update(ActionEvent event) throws IOException {

        String time = hours.getValue() + ":" + minute.getValue() + " " + AP.getValue();
        String query = "UPDATE ms_apb SET "
                + "DATE=?,"
                + "TIME=? WHERE id = '" + Id + "'";

        try {
            con = Connectionprovider.getConnection();
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setDate(1, java.sql.Date.valueOf(DATE.getValue()));
            pstm.setString(2, time);

            pstm.executeUpdate();

            root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            JOptionPane.showMessageDialog(null, "Update Successfully");
        } catch (SQLException e) {
            System.out.print(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//      Slider animation

        scroll.setTranslateX(-800);
        menuback.setTranslateX(-800);

        menu.setOnMouseClicked(event -> {
            TranslateTransition slideMenu = new TranslateTransition(); // Transition for the menu
            slideMenu.setDuration(Duration.seconds(0.4));
            slideMenu.setNode(scroll); // Set the menu as the node to animate
            slideMenu.setToX(0);

            TranslateTransition slideBack = new TranslateTransition(); 
            slideBack.setDuration(Duration.seconds(0.4));
            slideBack.setNode(menuback); 
            slideBack.setToX(0);

            ParallelTransition parallelTransition = new ParallelTransition(slideMenu, slideBack); 
            parallelTransition.play();

            parallelTransition.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slideMenu = new TranslateTransition(); 
            slideMenu.setDuration(Duration.seconds(0.4));
            slideMenu.setNode(scroll); 
            slideMenu.setToX(-800);

            TranslateTransition slideBack = new TranslateTransition();
            slideBack.setDuration(Duration.seconds(0.4));
            slideBack.setNode(menuback); 
            slideBack.setToX(-800);

            ParallelTransition parallelTransition = new ParallelTransition(slideMenu, slideBack);
            parallelTransition.play();

            parallelTransition.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });

        DATE.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }

        });
        FillcomboBox();
    }

    private void FillcomboBox() {
        AP.setItems(FXCollections.observableArrayList("AM", "PM"));

        ObservableList<String> hoursliList = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            hoursliList.add(String.format("%02d", i));
        }
        hours.setItems(hoursliList);

        ObservableList<String> minutelist = FXCollections.observableArrayList();
        for (int j = 0; j <= 59; j++) {
            minutelist.add(String.format("%02d", j));
        }
        minute.setItems(minutelist);

    }

    public void setTextField(int Id, String name, String phone, LocalDate date, String time) {
        this.Id = Id;
        NAME.setText(name);
        PHONE.setText(phone);
        DATE.setValue(date);

        // Parse the time string to populate hours, minutes, and AM/PM
        String[] timeParts = time.split(" ");
        String[] hourMinute = timeParts[0].split(":");
        hours.setValue(hourMinute[0]);
        minute.setValue(hourMinute[1]);
        AP.setValue(timeParts[1]);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }
    
//    Redirect pages
    
    @FXML
    public void Dashboard(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Im(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Product.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Report(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Report.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Reward(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Reward.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Setting(ActionEvent event) throws IOException  {
//        root = FXMLLoader.load(getClass().getResource("../FXML/Reward.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
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
    public void billing(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_list(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/CUSTOMERLIST.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void cu_reg(ActionEvent event) throws IOException  {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerregistration.fxml"));
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

    @FXML
    public void blhistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billinghistory.fxml"));
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

}
