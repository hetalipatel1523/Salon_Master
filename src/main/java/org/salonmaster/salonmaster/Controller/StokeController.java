package org.salonmaster.salonmaster.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;
import org.salonmaster.salonmaster.GETSET.Stokegetset;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StokeController implements Initializable {
    
    @FXML
    private Button ADD_PRODUCT;

    @FXML
    private Button APB_LIST;

    @FXML
    private Button BILLING;

    @FXML
    private Button CU_LIST;

    @FXML
    private Button CU_REG;

    @FXML
    private Button Im;

    @FXML
    private Button Report;

    @FXML
    private Button Reward;

    @FXML
    private Button Setting;

    @FXML
    private TableView<Stokegetset> lowstoke;

    @FXML
    private TableColumn<Stokegetset, String> name;

    @FXML
    private PieChart piechart;
    


    @FXML
    private TableColumn<Stokegetset, Integer> pis;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Connection con;
    private PreparedStatement pstm;
    private ResultSet rs;
    
        @FXML
    public void report(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Report.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



//    lowstock  table 
    ObservableList<Stokegetset> lowstoc = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        pis.setCellValueFactory(new PropertyValueFactory<>("pis"));

        try {
            con = Connectionprovider.getConnection();
            String q = "SELECT item, stock FROM ms_im WHERE ps = 'Product' AND stock BETWEEN 0 AND 15";
            pstm = con.prepareStatement(q);
            rs = pstm.executeQuery();

            while (rs.next()) {
                String dbitem = rs.getString("item");
                Integer dbQty = rs.getInt("stock");

                Stokegetset reportgetset = new Stokegetset(dbitem, dbQty);
                lowstoc.add(reportgetset);
            }

            lowstoke.setItems(lowstoc);
        } catch (SQLException e) {
            System.err.println(e);

        }
        // Make columns not movable
        name.setReorderable(false);
        pis.setReorderable(false);

//pia chart
        try {
            con = Connectionprovider.getConnection();
            String sql = "SELECT category, COUNT(stock) AS stock_count FROM ms_im GROUP BY category";

            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (rs.next()) {
                String category = rs.getString("category");
                int stockCount = rs.getInt("stock_count");
                pieChartData.add(new PieChart.Data(category, stockCount));
            }
            piechart.setData(pieChartData);

        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    
//    redirect pages
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
    public void Setting(ActionEvent event) {

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
}
