package org.salonmaster.salonmaster.Controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateproduct implements Initializable {

    @FXML
    private Button DESKTOP;

    @FXML
    private Button Update_product;

    @FXML
    private TextField p_catagory;

    @FXML
    private TextField p_name;

    @FXML
    private TextField p_price;

    @FXML
    private TextField p_quantity;

    @FXML
    private TextField p_s;

    private int Id;
    private boolean Update;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane Slider;

    @FXML
    private TextField productsearch;

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void Update(ActionEvent event) {

        String query = "UPDATE ms_im SET "
                + "item=?,"
                + "price=?,"
                + "stock=?,"
                + "category=?,"
                + "ps = ? "
                + "WHERE id = ?;";

//        '" + Id + "'"
        try {
            con = Connectionprovider.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, p_name.getText());
            pstmt.setString(2, p_price.getText());
            pstmt.setInt(3, Integer.parseInt(p_quantity.getText()));
            pstmt.setString(4, p_catagory.getText());
            pstmt.setString(5, p_s.getText());
            pstmt.setInt(6, Id);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Product updated successfully");
            p_name.setText("");
            p_price.setText("");
            p_quantity.setText("");
            p_catagory.setText("");
            p_s.setText("");

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    @FXML
    public void desktop(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        Slider animation
        Slider.setTranslateX(-800);

        menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(Slider);

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
            slide.setNode(Slider);

            slide.setToX(-800);
            slide.play();

            slide.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });
        productsearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String query = "SELECT item, price,stock,category,ps FROM ms_im WHERE item = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, newValue);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        p_name.setText(rs.getString("item"));
                        p_price.setText(rs.getString("price"));
                        p_quantity.setText(rs.getString("stock"));
                        p_catagory.setText(rs.getString("category"));
                        p_s.setText(rs.getString("ps"));
                    }

                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
            }
        });

        try {
            con = Connectionprovider.getConnection();

            String searchquery = "SELECT item FROM ms_im;";
            pstmt = con.prepareStatement(searchquery);
            rs = pstmt.executeQuery();
            ObservableList<String> searchlist = FXCollections.observableArrayList();

            while (rs.next()) {
                searchlist.add(rs.getString("item"));
            }

            TextFields.bindAutoCompletion(productsearch, searchlist);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void setTextField(int Id, String item, int stock, String price, String category, String Ps) {
        this.Id = Id;
        p_name.setText(item);
        p_quantity.setText(String.valueOf(stock)); // Convert int to String for setText
        p_price.setText(price);
        p_catagory.setText(category);
        p_s.setText(Ps);
    }

    void setUpdate(boolean b) {
        this.Update = b;
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
    public void Im(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Product.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void Report(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmast/org/salonmaster/salonmaster/FXML/Report.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Reward(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmast/org/salonmaster/salonmaster/FXML/Reward.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Setting(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("../FXML/Reward.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
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
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
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
    public void cu_reg(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerregistration.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
