package org.salonmaster.salonmaster.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.salonmaster.salonmaster.Connectionprovider.Connectionprovider;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

public class AppointmentdeskController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

   @FXML
    private Button ADD_PRODUCT;

    @FXML
    private Button APB_LIST;

    @FXML
    private Button Billing;

    @FXML
    private Button Blhistory;

    @FXML
    private Button CU_LIST;

    @FXML
    private Button CU_REG;

    @FXML
    private Button Emp;

    @FXML
    private Button Im;

    @FXML
    private Button Report;

    @FXML
    private Button Reward;

    @FXML
    private Button Setting;

    @FXML
    private Button booking;

    @FXML
    private FlowPane calendar;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private Text month;

    @FXML
    private StackPane navSlider;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Text year;


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void Setting(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Setting.fxml"));
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
    public void cu_reg(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Customerregistration.fxml"));
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
    public void add_product(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Inventory.fxml"));
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
    public void billing(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/Billing.fxml"));
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
    public void booking(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/salonmaster/salonmaster/FXML/BookAppointment.fxml"));
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
    
    
    private Map<LocalDate, Integer> appointmentCountMap = new HashMap<>();

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    public void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    public void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonthFromDatabase(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7.1) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6.1) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i) - dateOffset;
                if (calculatedDate > 0 && calculatedDate <= monthMaxDate) {
                    Text date = new Text(String.valueOf(calculatedDate));
                    double textTranslationY = -(rectangleHeight / 2) * 0.75;
                    date.setTranslateY(textTranslationY);
                    stackPane.getChildren().add(date);

                    List<CalendarActivity> calendarActivities = calendarActivityMap.get(calculatedDate);
                    int totalCount = calendarActivities != null ? calendarActivities.size() : 0;
                    if (totalCount > 0) {
                        ZonedDateTime selectedDate = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate, 0, 0, 0, 0, dateFocus.getZone());
                        createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane, totalCount, selectedDate);
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == calculatedDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }

                stackPane.setOnMouseClicked(event -> {
                    try {
                        int currentDate = calculatedDate; // Assign calculatedDate to currentDate
                        ZonedDateTime selectedDate = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate, 0, 0, 0, 0, dateFocus.getZone());

                        showAppointmentsForDate(selectedDate);
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                });
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane, int total, ZonedDateTime selectedDate) {
        VBox container = new VBox(); // Container for date and counting

        // Text for the date
        Text dateText = new Text(String.valueOf(selectedDate.getDayOfMonth()));
        dateText.setStyle("-fx-font-weight: bold;"); // Make the date bold
        container.getChildren().add(dateText);

        // Text for the counting
        Text countText = new Text("Total: " + total);
        countText.setStyle("-fx-font-size: 14;"); // Adjust font size as needed
        container.getChildren().add(countText);

        String color;
        try {
            String q = "SELECT red, green, orange FROM ms_setting";
            pstmt = con.prepareStatement(q);
            rs = pstmt.executeQuery();

            int checkred = 0;
            int checkgreen = 0;
            int checkorange = 0;

            while (rs.next()) {
                checkred = rs.getInt("red");
                checkgreen = rs.getInt("green");
                checkorange = rs.getInt("orange");
            }

            if (total <= checkgreen) {
                color = "green";
            } else if (total >= checkgreen && total <= checkorange) {
                color = "orange";
            } else {
                color = "red";
            }
        } catch (SQLException e) {
            System.out.println(e);
            color = "blue";
        }

        container.setStyle("-fx-background-color: " + color);

        container.setPrefSize(rectangleWidth, rectangleHeight);
        container.setAlignment(Pos.TOP_CENTER);
        stackPane.getChildren().add(container);

        stackPane.setOnMouseClicked(event -> {
            try {
                showAppointmentsForDate(selectedDate);
            } catch (SQLException e) {
                System.out.println(e);
            }
        });
    }

    private void showAppointmentsForDate(ZonedDateTime selectedDate) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/salonmaster/salonmaster/FXML/APPOINMENTLIST.fxml"));
            Parent newRoot = loader.load();

            APPOINMENTLISTController controller = loader.getController();
            controller.showAppointmentsForDate(selectedDate);

            // Check if the FlowPane is part of a scene
            if (calendar.getScene() != null) {
                // Get the current stage
                Stage currentStage = (Stage) calendar.getScene().getWindow();
                // Set the scene's root to the new root containing the appointments for the selected date
                currentStage.getScene().setRoot(newRoot);
                currentStage.setTitle("Appointments list");
                currentStage.show();
            } else {
//           
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, new ArrayList<>(List.of(activity)));
            } else {
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonthFromDatabase(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();

        try {
            con = Connectionprovider.getConnection();
// hide previous date in calender
            String query = "SELECT date, COUNT(*) AS appointment_count "
                    + " FROM ms_apb "
                    + " WHERE EXTRACT(MONTH FROM date) = ? "
                    + " AND EXTRACT(YEAR FROM date) = ? "
                    + " AND date >=  CURRENT_DATE  "
                    + " GROUP BY date; ";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, dateFocus.getMonthValue());
                preparedStatement.setInt(2, dateFocus.getYear());
//                preparedStatement.setObject(3, dateFocus.toLocalDate());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int count = resultSet.getInt("appointment_count");
                        ZonedDateTime appointmentDateTime = resultSet.getTimestamp("date").toLocalDateTime().atZone(dateFocus.getZone());

                        for (int i = 0; i < count; i++) {
                            calendarActivities.add(new CalendarActivity(appointmentDateTime));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return createCalendarMap(calendarActivities);
    }

}
