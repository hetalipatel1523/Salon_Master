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
import org.salonmaster.salonmaster.GETSET.custgeset;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerlistController implements Initializable {

    @FXML
    private Button DELETE;

    @FXML
    private Button EDIT;

    @FXML
    private TextField Searchbar;

    @FXML
    private Button search;

    @FXML
    private TableView<custgeset> TABLE;

    @FXML
    private TableColumn<custgeset, String> name;

    @FXML
    private TableColumn<custgeset, String> phone_no;

    @FXML
    private TableColumn<custgeset, LocalDate> c_birthday;

    @FXML
    private TableColumn<custgeset, LocalDate> c_anniversary;

    @FXML
    private TableColumn<custgeset, String> gender;

    @FXML
    private TableColumn<custgeset, Integer> reward;

    @FXML
    private Button Deskbord;

    @FXML
    private StackPane Slider;

    @FXML
    private StackPane navSlider;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private ScrollPane scroll;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private FilteredList<custgeset> filteredData;
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    ObservableList<custgeset> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //        horizontal off
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
        
         //        Search bar 
        search.setOnMouseClicked(event -> {
            Searchbar.setVisible(!Searchbar.isVisible());
        });

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone_no.setCellValueFactory(new PropertyValueFactory<>("phone_no"));
        c_birthday.setCellValueFactory(new PropertyValueFactory<>("c_birthday"));
        c_anniversary.setCellValueFactory(new PropertyValueFactory<>("c_anniversary"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        reward.setCellValueFactory(new PropertyValueFactory<>("reward"));

        try {
            con = Connectionprovider.getConnection();
            String q = "SELECT "
                    + " ms_cureg.id, "
                    + " ms_user.name, "
                    + " ms_user.phone_no, "
                    + " ms_cureg.c_birthday, "
                    + " ms_cureg.c_anniversary, "
                    + " ms_cureg.gender, "
                    + " COALESCE(ms_rew.reward, NULL) AS reward "
                    + " FROM "
                    + " ms_cureg "
                    + " JOIN "
                    + " ms_user ON ms_cureg.userid = ms_user.id "
                    + " LEFT JOIN "
                    + " ms_rew ON ms_user.phone_no = ms_rew.number "
                    + " ORDER BY ms_cureg.id ASC;";

            pstmt = con.prepareStatement(q);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int dbid = rs.getInt("id");
                String dbname = rs.getString("name");
                String dbphone_no = rs.getString("phone_no");
                Date dbc_birthday = rs.getDate("c_birthday");
                Date dbc_anniversary = rs.getDate("c_anniversary");
                String dbgender = rs.getString("gender");
                int dbreward = rs.getInt("reward");

                custgeset Custgeset = new custgeset(dbid, dbname, dbphone_no, dbc_birthday, dbc_anniversary, dbgender, dbreward);
                list.add(Custgeset);
            }
            filteredData = new FilteredList<>(list, b -> true);
            Searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(custgeset -> {

                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchkeyword = newValue.toLowerCase();

                    if (custgeset.getName().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else if (custgeset.getPhone_no().toLowerCase().contains(searchkeyword)) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });

            SortedList<custgeset> sorteData = new SortedList<>(filteredData);

            sorteData.comparatorProperty().bind(TABLE.comparatorProperty());

            TABLE.setItems(sorteData);
            TABLE.refresh();

        } catch (SQLException e) {
            System.out.println(e);
        }

        // Make columns not movable
        name.setReorderable(false);
        phone_no.setReorderable(false);
        c_birthday.setReorderable(false);
        c_anniversary.setReorderable(false);
        gender.setReorderable(false);
        reward.setReorderable(false);


    }

    @FXML
    public void delete(ActionEvent event) throws SQLException {
        custgeset Custgeset = TABLE.getSelectionModel().getSelectedItem();
        if (Custgeset == null) {
            Showalert();
            return;
        }
        showAlert("Confirm Delete", "Are you sure you want to delete this item?", e -> {
            try {
                String query = "DELETE FROM ms_cureg WHERE id = ?";
                Connection connection = Connectionprovider.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Custgeset.getId());
                preparedStatement.executeUpdate();
                list.remove(Custgeset);
                TABLE.refresh();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
    }

    @FXML
    public void edit(ActionEvent event) {
        custgeset Custgeset = TABLE.getSelectionModel().getSelectedItem();
        if (Custgeset == null) {
            Showalert();
            return;
        }
        Stage primaryStage = (Stage) TABLE.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org/salonmaster/salonmaster/FXML/Cu_update.fxml"));
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.out.println(ex);
            return;
        }

        Cu_updateController Cu_updateController = loader.getController();
        Cu_updateController.setUpdate(true);
        // Check for null values and set empty if any field is null
        Cu_updateController.setTextField(
                Custgeset.getId(),
                Custgeset.getName() != null ? Custgeset.getName() : "",
                Custgeset.getPhone_no() != null ? Custgeset.getPhone_no() : "",
                Custgeset.getC_birthday() != null ? Custgeset.getC_birthday() : null,
                Custgeset.getC_anniversary() != null ? Custgeset.getC_anniversary() : null,
                Custgeset.getGender() != null ? Custgeset.getGender() : ""
        );
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
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
