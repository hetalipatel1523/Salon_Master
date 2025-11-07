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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML
    private TextField p_name;

    @FXML
    private TextField p_price;

    @FXML
    private TextField p_quantity;

    @FXML
    private TextField p_catagory;

    @FXML
    private Button add_product;

    @FXML
    private Button DESKTOP;

    @FXML
    private ComboBox<String> p_s;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane Slider;
    @FXML
    private ScrollPane scroll;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private boolean update;
    private int Id;

    Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private boolean Update;

    @FXML
    public void submit(ActionEvent event) throws IOException {
        String ProductName = p_name.getText();
        String ProductPrice = p_price.getText();
       int ProductQuantity = p_quantity.getText().isEmpty() ? 0 : Integer.parseInt(p_quantity.getText());
        String ProductCategory = p_catagory.getText();
        String P_s = p_s.getValue();

        if (ProductName.isEmpty() || ProductPrice.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all details!");

        } else {
            try (Connection con = Connectionprovider.getConnection()) {
                String checkQuery = "SELECT COUNT(*) AS count FROM ms_im WHERE item = ?";
                pstmt = con.prepareStatement(checkQuery);
                pstmt.setString(1, ProductName);
                rs = pstmt.executeQuery();
                rs.next();
                int count = rs.getInt("count");

                if (count > 0) {
                    String updateQuery = "UPDATE ms_im SET price = ?, stock = ?, category = ?, ps = ?, item = ? WHERE item = ?";
                    pstmt = con.prepareStatement(updateQuery);
                    pstmt.setString(1, ProductPrice);
                    pstmt.setInt(2, ProductQuantity);
                    pstmt.setString(3, ProductCategory);
                    pstmt.setString(4, P_s);
                    pstmt.setString(5, ProductName);
                    pstmt.setString(6, ProductName);
                    pstmt.executeUpdate();

                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Product.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Product updated successfully");
                } else {
                    String insertQuery = "INSERT INTO ms_im(item, price, stock, category, ps) VALUES (?, ?, ?, ?, ?)";
                    pstmt = con.prepareStatement(insertQuery);
                    pstmt.setString(1, ProductName);
                    pstmt.setString(2, ProductPrice);
                    pstmt.setInt(3, ProductQuantity);
                    pstmt.setString(4, ProductCategory);
                    pstmt.setString(5, P_s);
                    pstmt.executeUpdate();

                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Product.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    JOptionPane.showMessageDialog(null, "Product added successfully");

                }
               
                p_name.setText("");
                p_price.setText("");
                p_quantity.setText("");
                p_catagory.setText("");
                p_s.setValue("");
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        p_name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String query = "SELECT id,item, price,stock,category,ps FROM ms_im WHERE item = ?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, newValue);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        p_name.setText(rs.getString("item"));
                        p_price.setText(rs.getString("price"));
                        p_quantity.setText(rs.getString("stock"));
                        p_catagory.setText(rs.getString("category"));
                        p_s.setValue(rs.getString("ps"));
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

//            TextFields.bindAutoCompletion(p_name, searchlist);
        } catch (SQLException e) {
            System.out.println(e);
        }

        FillComboBox();

        p_s.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Service".equals(newValue)) {
                p_quantity.setText("1");
                p_quantity.setEditable(false);
            } else {
                p_quantity.setText("0");
                p_quantity.setEditable(true);

            }
        });
    }

    private void FillComboBox() {
        p_s.setItems(FXCollections.observableArrayList("Product", "Service"));
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setTextField(int Id, String item, int stock, String price, String category, String Ps) {
        this.Id = Id;
        p_name.setText(item);
        p_quantity.setText(String.valueOf(stock));
        p_price.setText(price);
        p_catagory.setText(category);
        p_s.setValue(Ps);
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
    public void Setting(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Reward.fxml"));
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
