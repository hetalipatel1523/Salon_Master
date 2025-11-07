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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.Customer;
import org.salonmaster.salonmaster.GETSET.product;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ReportController implements Initializable {

    @FXML
    private VBox chartContainer;

    @FXML
    private VBox chartContainerT;

    @FXML
    private HBox headerHBox;

    @FXML
    private VBox vboxContainer;

    @FXML
    private TableColumn<Customer, String> itemname;

    @FXML
    private TableView<Customer> TopService;

    @FXML
    private TableColumn<Customer, Integer> Quantity;

    @FXML
    private TableColumn<product, Integer> Qty;

    @FXML
    private TableColumn<product, String> prodname;

    @FXML
    private TableView<product> producttable;


    @FXML
    private Button Stoke;
    
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

    private PreparedStatement pstm;
    private ResultSet rs;
    private Connection con;


    @FXML
    public void gostoke(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Stoke.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

//    Appointment
    private void Apbchart() {
        String APBquery = "SELECT "
                + "CASE "
                + "    WHEN EXTRACT(MONTH FROM date) = 1 THEN 'Jan' "
                + "    WHEN EXTRACT(MONTH FROM date) = 2 THEN 'Feb' "
                + "    WHEN EXTRACT(MONTH FROM date) = 3 THEN 'Mar' "
                + "    WHEN EXTRACT(MONTH FROM date) = 4 THEN 'Apr' "
                + "    WHEN EXTRACT(MONTH FROM date) = 5 THEN 'May' "
                + "    WHEN EXTRACT(MONTH FROM date) = 6 THEN 'Jun' "
                + "    WHEN EXTRACT(MONTH FROM date) = 7 THEN 'Jul' "
                + "    WHEN EXTRACT(MONTH FROM date) = 8 THEN 'Aug' "
                + "    WHEN EXTRACT(MONTH FROM date) = 9 THEN 'Sep' "
                + "    WHEN EXTRACT(MONTH FROM date) = 10 THEN 'Oct' "
                + "    WHEN EXTRACT(MONTH FROM date) = 11 THEN 'Nov' "
                + "    WHEN EXTRACT(MONTH FROM date) = 12 THEN 'Dec' "
                + "END AS month_string, "
                + "COUNT(*) AS appointment_count "
                + "FROM ms_apb "
                + "GROUP BY EXTRACT(MONTH FROM date), month_string "
                + "ORDER BY EXTRACT(MONTH FROM date) ASC";

        try {
            con = Connectionprovider.getConnection();
            XYChart.Series<String, Number> APBData = new XYChart.Series<>();
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

            pstm = con.prepareStatement(APBquery);
            rs = pstm.executeQuery();

            while (rs.next()) {
                APBData.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
                APBData.setName("Appointments");
                // Swap String and Number
            }

            barChart.getData().add(APBData);
//            barChart.setStyle("-fx-bar-fill: blue;");
            chartContainerT.getChildren().add(barChart);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void SALEchrt() {
        String APBquery = "SELECT "
                + "CASE "
                + "    WHEN EXTRACT(MONTH FROM date) = 1 THEN 'Jan' "
                + "    WHEN EXTRACT(MONTH FROM date) = 2 THEN 'Feb' "
                + "    WHEN EXTRACT(MONTH FROM date) = 3 THEN 'Mar' "
                + "    WHEN EXTRACT(MONTH FROM date) = 4 THEN 'Apr' "
                + "    WHEN EXTRACT(MONTH FROM date) = 5 THEN 'May' "
                + "    WHEN EXTRACT(MONTH FROM date) = 6 THEN 'Jun' "
                + "    WHEN EXTRACT(MONTH FROM date) = 7 THEN 'Jul' "
                + "    WHEN EXTRACT(MONTH FROM date) = 8 THEN 'Aug' "
                + "    WHEN EXTRACT(MONTH FROM date) = 9 THEN 'Sep' "
                + "    WHEN EXTRACT(MONTH FROM date) = 10 THEN 'Oct' "
                + "    WHEN EXTRACT(MONTH FROM date) = 11 THEN 'Nov' "
                + "    WHEN EXTRACT(MONTH FROM date) = 12 THEN 'Dec' "
                + "END AS month_string,"
                + "Sum(price) AS Total_count "
                + "FROM ms_bl "
                + "GROUP BY EXTRACT(MONTH FROM date), month_string "
                + "ORDER BY EXTRACT(MONTH FROM date) ASC";

        try {
            con = Connectionprovider.getConnection();
            XYChart.Series<String, Number> APBData = new XYChart.Series<>();
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

            pstm = con.prepareStatement(APBquery);
            rs = pstm.executeQuery();

            while (rs.next()) {
                APBData.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
                APBData.setName("Sale");

            }

            barChart.getData().add(APBData);

            chartContainer.getChildren().add(barChart);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    ObservableList<Customer> Service = FXCollections.observableArrayList();
    ObservableList<product> productlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
//            horizontal off
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
//        TO Display the  Appointment chart
        Apbchart();

        //        TO Display the  Sale chart
        SALEchrt();

//        Top Service Container 
        itemname.setCellValueFactory(new PropertyValueFactory<>("item"));
        Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TopService.setItems(Service);

        try {
            con = Connectionprovider.getConnection();
            String q = "SELECT item, COUNT(item) AS purchase_count FROM ms_bl where ps  = 'Service'GROUP BY item ORDER BY purchase_count DESC LIMIT 5";
            pstm = con.prepareStatement(q);
            rs = pstm.executeQuery();

            while (rs.next()) {
                String dbitem = rs.getString("item");
                Integer dbQty = rs.getInt("purchase_count");

                Customer reportgetset = new Customer(dbitem, dbQty);
                Service.add(reportgetset);
            }

            TopService.setItems(Service);
        } catch (SQLException e) {
            System.err.println(e);

        }

        // Make columns not movable
        itemname.setReorderable(false);
        Quantity.setReorderable(false);
        Qty.setReorderable(false);
        prodname.setReorderable(false);

        //Top Product container 
        prodname.setCellValueFactory(new PropertyValueFactory<>("item"));
        Qty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        producttable.setItems(productlist);

        try {
            con = Connectionprovider.getConnection();
            String q = "SELECT item, COUNT(item) AS product_count FROM ms_bl where ps  = 'Product'GROUP BY item ORDER BY product_count DESC LIMIT 5";
            pstm = con.prepareStatement(q);
            rs = pstm.executeQuery();

            while (rs.next()) {
                String dbitem = rs.getString("item");
                Integer dbqty = rs.getInt("product_count");

                product productgetset = new product(dbitem, dbqty);
                productlist.add(productgetset);
            }

            producttable.setItems(productlist);
        } catch (SQLException e) {
            System.err.println(e);

        }

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
    public void Dashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
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
