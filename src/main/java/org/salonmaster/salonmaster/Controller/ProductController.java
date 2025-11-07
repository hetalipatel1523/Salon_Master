package org.salonmaster.salonmaster.Controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.ProductData;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TableView<ProductData> inventoryproduct;

    @FXML
    private TableColumn<ProductData, Integer> id;

    @FXML
    private TableColumn<ProductData, String> item;

    @FXML
    private TableColumn<ProductData, Integer> stock;

    @FXML
    private TableColumn<ProductData, Double> price;

    @FXML
    private TableColumn<ProductData, String> category;

    @FXML
    private TableColumn<ProductData, String> ps;

    @FXML
    private StackPane Slider;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private Button Updateproduct;

    @FXML
    private StackPane navSlider;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchproduct;

    @FXML
    private Button search;

    private ObservableList<ProductData> productItemList = FXCollections.observableArrayList();
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Parent root;
    private Scene scene;
    private Stage stage;

    private FilteredList<ProductData> filteredData;

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

        Slider.setTranslateY(600);
        inventoryproduct.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(Slider);
            slide.setToY(0);
            slide.play();

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                TranslateTransition slideOut = new TranslateTransition(Duration.seconds(0.4), Slider);
                slideOut.setToY(600);
                slideOut.play();
            });
            pause.play();
        });

        //        Search bar 
        search.setOnMouseClicked(event -> {
            searchproduct.setVisible(!searchproduct.isVisible());
        });

        configureTableColumns();
        fetchDataFromDatabase();

        filteredData = new FilteredList<>(productItemList, b -> true);
        searchproduct.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ProductData -> {

                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String searchkeyword = newValue.toLowerCase();

                if (ProductData.getItem().toLowerCase().contains(searchkeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<ProductData> sorteData = new SortedList<>(filteredData);
        sorteData.comparatorProperty().bind(inventoryproduct.comparatorProperty());

        inventoryproduct.setItems(sorteData);
        inventoryproduct.refresh();
    }

    private void configureTableColumns() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item.setCellValueFactory(new PropertyValueFactory<>("item"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        ps.setCellValueFactory(new PropertyValueFactory<>("ps"));
    }

    private void fetchDataFromDatabase() {
        try {
            con = Connectionprovider.getConnection();
            String query = "SELECT * FROM ms_im";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductData product = new ProductData(
                        rs.getInt("id"),
                        rs.getString("item"),
                        rs.getInt("stock"),
                        rs.getString("price"),
                        rs.getString("category"),
                        rs.getString("ps")
                );
                productItemList.add(product);
            }

            inventoryproduct.setItems(productItemList);

        } catch (SQLException e) {
            System.out.println(e);
        }

        // Make columns not movable
        id.setReorderable(false);
        item.setReorderable(false);
        stock.setReorderable(false);
        price.setReorderable(false);
        category.setReorderable(false);
        ps.setReorderable(false);
    }

    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        loadFXML("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml");
    }

    @FXML
    public void Update(ActionEvent event) throws IOException {
        ProductData productData = inventoryproduct.getSelectionModel().getSelectedItem();
        if (productData == null) {
            Showalert();
            return;
        }

        Stage primaryStage = (Stage) inventoryproduct.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/salonmaster/salonmaster/FXML/Inventory.fxml")); // Path to Inventory.fxml
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }

        InventoryController inventoryController = loader.getController(); // Use InventoryController class
        inventoryController.setUpdate(true);
        inventoryController.setTextField(
                productData.getId(),
                productData.getItem(),
                productData.getStock(),
                productData.getPrice(),
                productData.getCategory(),
                productData.getPs() // Assuming getPs() returns Ps
        );
        scene = new Scene(root); // Declare scene here
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void delete(ActionEvent event) {

        ProductData selectedItem = inventoryproduct.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            Showalert();
        } else {
            showAlert("Confirm Delete", "Are you sure you want to delete this item?", e -> {
                deleteRecordFromDatabase(selectedItem);
                productItemList.remove(selectedItem);
//                updateTotal();
            });
        }

    }

    private void deleteRecordFromDatabase(ProductData productData) {
        try {
            con = Connectionprovider.getConnection();
            String query = "DELETE FROM ms_im WHERE id = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, productData.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

//    private void updateTotal() {
//    }
    private void loadFXML(String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) inventoryproduct.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content, EventHandler<ActionEvent> onConfirmation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Set header text to null to hide it
        alert.setContentText(content);

        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                onConfirmation.handle(null);
            }
        });
    }

    private void Showalert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Value Entered");
        alert.setHeaderText(null);
        alert.setContentText("Please select data from  table.");
        alert.showAndWait();
    }

    @FXML
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
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
