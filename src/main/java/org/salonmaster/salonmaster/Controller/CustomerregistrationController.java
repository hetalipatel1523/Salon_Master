package org.salonmaster.salonmaster.Controller;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerregistrationController implements Initializable {

    Connection con;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField cnameTextField;

    @FXML
    private TextField numTextField;

    @FXML
    private DatePicker AdateTextField;

    @FXML
    private DatePicker BdateTextField;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane Slider;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button deskbord;

    @FXML
    private ComboBox<String> GenTextFIeld;

    public void submit(ActionEvent event) throws IOException {
        String name = cnameTextField.getText();
        String phone_no = numTextField.getText();
        LocalDate birthDate = BdateTextField.getValue();
        LocalDate anniversaryDate = AdateTextField.getValue();
        String gen = GenTextFIeld.getValue();

//   
        if (name.isEmpty() || phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name and Contact required");
        } else if (!phone_no.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid Phone Number");
        } else {
            try {
                con = Connectionprovider.getConnection();

                String checkphone = "SELECT id FROM ms_user WHERE phone_no=?";
                try (PreparedStatement checkStmt = con.prepareStatement(checkphone)) {
                    checkStmt.setString(1, phone_no);
                    ResultSet userResultSet = checkStmt.executeQuery();

                    if (userResultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Customer already exists");
                    } else {
                        String q = "INSERT INTO ms_user (name, phone_no) VALUES (?,?) RETURNING id";
                        try (PreparedStatement userstatement = con.prepareStatement(q)) {
                            userstatement.setString(1, name);
                            userstatement.setString(2, phone_no);

                            try (var rs = userstatement.executeQuery()) {
                                if (rs.next()) {
                                    int userid = rs.getInt(1);

                                    String cuquery = "INSERT INTO ms_cureg (userid, c_birthday, c_anniversary, gender) VALUES (?,?,?,?)";
                                    try (var custatement = con.prepareStatement(cuquery)) {
                                        custatement.setInt(1, userid);

                                        if (birthDate != null) {
                                            custatement.setDate(2, java.sql.Date.valueOf(birthDate));
                                        } else {
                                            custatement.setNull(2, java.sql.Types.DATE);
                                        }

                                        if (anniversaryDate != null) {
                                            custatement.setDate(3, java.sql.Date.valueOf(anniversaryDate));
                                        } else {
                                            custatement.setNull(3, java.sql.Types.DATE);
                                        }

                                        custatement.setString(4, gen);
                                        custatement.executeUpdate();
                                    }

                                    root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
                                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.show();
                                    JOptionPane.showMessageDialog(null, "Registration successfully");
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        AdateTextField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        BdateTextField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        FillComboBox();
        

        //horizontal off
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

    }

    private void FillComboBox() {
        GenTextFIeld.setItems(FXCollections.observableArrayList("MALE", "FEMALE"));
    }

    public void setAppoinment(String name, String phone_no) {
        cnameTextField.setText(name);
        numTextField.setText(phone_no);

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
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Setting.fxml"));
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
