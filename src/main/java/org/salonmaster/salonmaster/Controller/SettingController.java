package org.salonmaster.salonmaster.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private Button ADD_PRODUCT;

    @FXML
    private Button APB_LIST;

    @FXML
    private Button Billing;

    @FXML
    private Button Blhistory;

    @FXML
    private Button CU_LIST;

    @FXML
    private Button CU_REG;

    @FXML
    private Button Dashboard;

    @FXML
    private Button Emp;

    @FXML
    private Button Im;

    @FXML
    private Button Report;

    @FXML
    private Button Reward;

    @FXML
    private TextField Rs;

    @FXML
    private Label lab;

    @FXML
    private TextField maxg;

    @FXML
    private TextField maxo;

    @FXML
    private TextField maxr;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane navSlider;

    @FXML
    private TextField postfix;

    @FXML
    private TextField prefix;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ToggleButton tb;

    private Connection con;
    private PreparedStatement pstmt;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void save(ActionEvent event) throws IOException {
        String maxRText = maxr.getText();
        String maxOText = maxo.getText();
        String maxGText = maxg.getText();
        String rsText = Rs.getText();
        String post = postfix.getText();
        String prif = prefix.getText();
        short status = (short) (tb.isSelected() ? 1 : 0);

        try {
            int red = maxRText.isEmpty() ? 0 : Integer.parseInt(maxRText);
            int orange = maxOText.isEmpty() ? 0 : Integer.parseInt(maxOText);
            int green = maxGText.isEmpty() ? 0 : Integer.parseInt(maxGText);
            int amount = rsText.isEmpty() ? 0 : Integer.parseInt(rsText);

            con = Connectionprovider.getConnection();
            String q = "INSERT INTO ms_setting(amount, red, orange, green, postfix, prefix, ns_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(q);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, red);
            pstmt.setInt(3, orange);
            pstmt.setInt(4, green);
            pstmt.setString(5, post);
            pstmt.setString(6, prif);
            pstmt.setShort(7, status);

            pstmt.executeUpdate();

            if (red > 0 && green > 0 && orange > 0 && amount == 0 && post.isEmpty() && prif.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Range saved successfully!");
            } else if (amount > 0 && red == 0 && green == 0 && orange == 0 && post.isEmpty() && prif.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Reward points saved!");
            } else if (post.isEmpty() && prif.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Prefix and postfix saved!");
            } else {
                JOptionPane.showMessageDialog(null, "Saved.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numeric values.");
        } catch (SQLException e) {
            System.out.println(e);
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//  Slider animation
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

//------------------------------------------------------------------------
        lab.setVisible(false);
        tb.setOnAction(event -> {
            if (tb.isSelected()) {
                tb.setText("Disable");
                lab.setText("Turn On");
                tb.setStyle("-fx-text-fill: white; -fx-background-color: rgba(80, 125, 188, 1);;");
            } else {
                tb.setText("Enable");
                lab.setText("Turn Off");
                tb.setStyle("-fx-text-fill: white; -fx-background-color: rgba(4, 8, 15, 1);");
            }
            lab.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> lab.setVisible(false)));
            timeline.play();
        });

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

}
