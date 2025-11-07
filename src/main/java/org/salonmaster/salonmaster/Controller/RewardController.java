package org.salonmaster.salonmaster.Controller;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.RewardItem;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RewardController implements Initializable {

    @FXML
    private Button enter;
    
    @FXML
    private Button search1;

    @FXML
    private TableView<RewardItem> rewardtable;

    @FXML
    private TableColumn<RewardItem, String> custnum;

    @FXML
    private TableColumn<RewardItem, Integer> reward;

    @FXML
    private TextField search;

    @FXML
    private TextField minusreward;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private StackPane navSlider;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button minus;

    private Connection connection;
    private FilteredList<RewardItem> filteredData;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //        horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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

        search1.setOnMouseClicked(event -> {
            search.setVisible(!search.isVisible());
        });

        // Establish database connection
        connection = Connectionprovider.getConnection();

        populateRewardTable();
    }

    @FXML
    public void Deshboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void populateRewardTable() {
        try {
            String query = "SELECT number, reward FROM ms_rew";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

                ObservableList<RewardItem> dataList = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    RewardItem data = new RewardItem(
                            resultSet.getString("number"),
                            resultSet.getInt("reward")
                    );
                    dataList.add(data);
                }

                custnum.setCellValueFactory(cellData -> cellData.getValue().custnumProperty());
                reward.setCellValueFactory(cellData -> cellData.getValue().rewardProperty().asObject());

                // Set up the filtered data and search functionality
                filteredData = new FilteredList<>(dataList, b -> true);
                search.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(rewardItem -> {
                        if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                            return true;
                        }

                        String searchKeyword = newValue.toLowerCase();
                        String custnumValue = rewardItem.getCustnum();

                        // Handle null values
                        if (custnumValue == null) {
                            custnumValue = "";
                        }

                        return custnumValue.toLowerCase().contains(searchKeyword);
                    });
                });

                // Set up sorting
                SortedList<RewardItem> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(rewardtable.comparatorProperty());

                rewardtable.setItems(sortedData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Make columns not movable
        custnum.setReorderable(false);
        reward.setReorderable(false);
    }

    @FXML
    private void handleMinusButton(ActionEvent event) {
        // Get the selected item from the table
        RewardItem selectedRewardItem = rewardtable.getSelectionModel().getSelectedItem();
        if (selectedRewardItem == null) {
            // No item selected, display an alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Item Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an item from the table to proceed.");
            alert.showAndWait();
            return;
        }

        String inputText = minusreward.getText().trim();
        if (inputText.isEmpty()) {
            // No digit entered, display an alert
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Value Entered");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a value in the reward field to proceed.");
            alert.showAndWait();
            return;
        }

        JOptionPane.showMessageDialog(null, "Reward redeem successfully");

        try {
            // Get the value to subtract from the reward from the minusreward TextField
            int subtractionValue = Integer.parseInt(inputText);

            if (selectedRewardItem.getReward() == 0 && subtractionValue > 0) {
                // Reward is already 0, cannot be decreased further
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Reward Already 0");
                alert.setHeaderText(null);
                alert.setContentText("The reward is already 0. You cannot decrease it further.");
                alert.showAndWait();
                return;
            }

            // Subtract the value from the reward
            int updatedReward = selectedRewardItem.getReward() - subtractionValue;
            if (updatedReward < 0) {
                updatedReward = 0; // Ensure reward doesn't go below 0
            }
            selectedRewardItem.setReward(updatedReward);

            // Reflect the updated reward in the TableView
            rewardtable.refresh();

            // Update the database with the new reward value
            updateRewardInDatabase(selectedRewardItem.getCustnum(), updatedReward);
        } catch (NumberFormatException e) {
            // Handle the case where the input in minusreward is not a valid integer
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid integer value in the minusreward field.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void updateRewardInDatabase(String custnum, int updatedReward) {
        try {
            // Update the correct table in the database
            String updateQuery = "UPDATE ms_rew SET reward = ? WHERE number = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, updatedReward);
                updateStatement.setString(2, custnum);
                updateStatement.executeUpdate();
                rewardtable.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
