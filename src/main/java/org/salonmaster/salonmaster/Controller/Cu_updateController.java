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
import java.sql.*;
import java.util.ResourceBundle;

public class Cu_updateController implements Initializable {

    @FXML
    private TextField cnameTextField;
    @FXML
    private TextField numTextField;

    @FXML
    private ComboBox<String> GenTextFIeld;
    @FXML
    private Button Update;

    @FXML
    private DatePicker Adatetextfield;

    @FXML
    private DatePicker Bdatetextfield;
    @FXML
    private Button Desktop;

    private Connection con;
    private boolean update;
    private int Id;
    private PreparedStatement pstm;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane navSlider;

    @FXML
    private ScrollPane scroll;

    boolean condition = true;

    @FXML
    public void UPDATE(ActionEvent event) throws IOException {
        con = Connectionprovider.getConnection();

        String name = cnameTextField.getText();
        String phone_no = numTextField.getText();
//        LocalDate birthDate = BdateTextField.getValue();
//        LocalDate anniversaryDate = AdateTextField.getValue();
        String gen = GenTextFIeld.getValue();

        if (name.isEmpty() || phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name and Contact required");
        } else if (!phone_no.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid Phone Number");
        } else {

            String query = "BEGIN; "
                    + "UPDATE ms_user "
                    + "SET name = ? , phone_no = ? "
                    + "WHERE id = ? ; "
                    + "UPDATE ms_cureg "
                    + "SET c_birthday = ?, c_anniversary = ?, gender = ? "
                    + "WHERE id = ?; "
                    + "COMMIT;";

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, cnameTextField.getText());
                pstmt.setString(2, numTextField.getText());
                pstmt.setInt(3, Id);
                if (Bdatetextfield != null && Bdatetextfield.getValue() != null) {
                    pstmt.setDate(4, Date.valueOf(Bdatetextfield.getValue()));
                } else {
                    pstmt.setNull(4, Types.DATE);
                }

                if (Adatetextfield != null && Adatetextfield.getValue() != null) {
                    pstmt.setDate(5, Date.valueOf(Adatetextfield.getValue()));
                } else {
                    pstmt.setNull(5, Types.DATE);
                }
                pstmt.setString(6, GenTextFIeld.getValue());
                pstmt.setInt(7, Id);
                pstmt.executeUpdate();

                root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerlist.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                JOptionPane.showMessageDialog(null, "Update successfully");

            } catch (SQLException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage());
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    public void setTextField(int id, String Name, String phone_no, Date c_birthday, Date c_anniversary, String gender) {
        this.Id = id;
        cnameTextField.setText(Name);
        numTextField.setText(phone_no);
        if (c_birthday != null) {
            Bdatetextfield.setValue(c_birthday.toLocalDate()); // <-- Line 163
        } else {
            Bdatetextfield.setValue(null);
        }
        if (c_anniversary != null) {
            Adatetextfield.setValue(c_anniversary.toLocalDate());
        } else {
            Adatetextfield.setValue(null);
        }
        GenTextFIeld.setValue(gender);
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    private void FillComboBox() {
        GenTextFIeld.setItems(FXCollections.observableArrayList("MALE", "FEMALE"));
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
