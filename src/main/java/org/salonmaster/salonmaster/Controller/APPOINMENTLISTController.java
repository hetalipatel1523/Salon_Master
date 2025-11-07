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
import org.salonmaster.salonmaster.GETSET.APBLIST;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class APPOINMENTLISTController implements Initializable {

    @FXML
    private TableColumn<APBLIST, String> name;

    @FXML
    private TableColumn<APBLIST, String> phone_no;

    @FXML
    private TableColumn<APBLIST, String> date;

    @FXML
    private TableColumn<APBLIST, String> time;

    @FXML
    private TableView<APBLIST> TABLE;

    @FXML
    private TextField searchbar;

    @FXML
    private Button DELETE;

    @FXML
    private Button EDIT;

    @FXML
    private Button Bill;

    @FXML
    private Button deskbord;

    @FXML
    private StackPane Slider;

    @FXML
    private StackPane navSlider;

    @FXML
    private Button search;
    

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void navigatedaskbord(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Appointmentdesk.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private FilteredList<APBLIST> filteredData;

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    ObservableList<APBLIST> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //horizontal off
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        slider animation
        Slider.setTranslateY(600);
        TABLE.setOnMouseClicked(event -> {
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
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone_no.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));

        //navslider
        scroll.setTranslateX(-800);
        menu.setOnMouseClicked(event -> {
            TranslateTransition slider = new TranslateTransition();
            slider.setDuration(Duration.seconds(0.4));
            slider.setNode(scroll);

            slider.setToX(0);
            slider.play();

            slider.setOnFinished((ActionEvent e) -> {
                menu.setVisible(false);
                menuback.setVisible(true);
            });
        });

        menuback.setOnMouseClicked(event -> {
            TranslateTransition slider = new TranslateTransition();
            slider.setDuration(Duration.seconds(0.4));
            slider.setNode(scroll);

            slider.setToX(-800);
            slider.play();

            slider.setOnFinished((ActionEvent e) -> {
                menu.setVisible(true);
                menuback.setVisible(false);
            });
        });

        //        Search bar 
        search.setOnMouseClicked(event -> {
            searchbar.setVisible(!searchbar.isVisible());
        });

    }

    @FXML
    public void delete(ActionEvent event) {
        APBLIST apblist = TABLE.getSelectionModel().getSelectedItem();

        if (apblist == null) {
            Showalert(); // Show warning if no data is selected from the table
            return; // Exit the method if no data is selected
        }

        showAlert("Confirm Delete", "Are you sure you want to delete this item?", e -> {
            try {
                String query = "DELETE FROM ms_apb WHERE id = ?";
                Connection connection = Connectionprovider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, apblist.getId());
                preparedStatement.execute();

                list.remove(apblist);
                TABLE.refresh();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    @FXML
    public void edit(ActionEvent event) {
        APBLIST apblist = TABLE.getSelectionModel().getSelectedItem();
        if (apblist == null) {
            Showalert();
            return;
        }

        Stage primaryStage = (Stage) TABLE.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/salonmaster/salonmaster/FXML/update.fxml"));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }

        UpdateController UpdateController = loader.getController();
        UpdateController.setUpdate(true);
        UpdateController.setTextField(
                apblist.getId(),
                apblist.getName(),
                apblist.getPhone_no(),
                apblist.getDate(),
                apblist.getTime()
        );
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void bill(ActionEvent event) {
        APBLIST apblist = TABLE.getSelectionModel().getSelectedItem();
        if (apblist == null) {
            Showalert();
            return;
        }

        Stage primaryStage = (Stage) TABLE.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }

        BillingController billingController = loader.getController();
        billingController.setUpdate(true);
        billingController.setTextField(
                apblist.getName(),
                apblist.getPhone_no()
        );
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void showAppointmentsForDate(ZonedDateTime selectedDate) throws SQLException {
        try {

            TABLE.setEditable(true);
            con = Connectionprovider.getConnection();
            String q = "SELECT ms_apb.id, ms_user.name, ms_user.phone_no, ms_apb.date, ms_apb.time FROM ms_apb JOIN ms_user ON ms_apb.user_id = ms_user.id WHERE ms_apb.date = ? ORDER BY ms_apb.id ASC;";
            pstmt = con.prepareStatement(q);
//            pstmt.setObject(1, selectedDate);

            LocalDate localDate = selectedDate.toLocalDate();

            pstmt.setObject(1, localDate);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int dbid = rs.getInt("id");
                String dbname = rs.getString("name");
                String dbphone_no = rs.getString("phone_no");
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate dbdate = sqlDate.toLocalDate();
                String dbtime = rs.getString("time");

                APBLIST apblist = new APBLIST(dbid, dbname, dbphone_no, dbdate, dbtime);
                list.add(apblist);
                filteredData = new FilteredList<>(list, b -> true);
                searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(APBLIST -> {

                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchkeyword = newValue.toLowerCase();

                        if (APBLIST.getName().toLowerCase().contains(searchkeyword)) {
                            return true;
                        } else if (APBLIST.getPhone_no().toLowerCase().contains(searchkeyword)) {
                            return true;
                        } else {
                            return false;
                        }

                    });
                });
                SortedList<APBLIST> sorteData = new SortedList<>(filteredData);

                sorteData.comparatorProperty().bind(TABLE.comparatorProperty());

                TABLE.setItems(sorteData);
                TABLE.refresh();

            }
        } catch (SQLException e) {
            System.err.println(e);
        }

        // Make columns not movable
        name.setReorderable(false);
        phone_no.setReorderable(false);
        date.setReorderable(false);
        time.setReorderable(false);

    }

    private void Showalert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Value Entered");
        alert.setHeaderText(null);
        alert.setContentText("Please select data from  table.");
        alert.showAndWait();
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
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Setting.fxml"));
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
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerlist.fxml"));
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
