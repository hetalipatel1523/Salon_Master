module org.salonmaster.salonmaster{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires org.postgresql.jdbc;


    opens org.salonmaster.salonmaster.main to javafx.fxml;
    opens org.salonmaster.salonmaster.Controller to javafx.fxml;
    opens org.salonmaster.salonmaster.GETSET to javafx.base;

    exports org.salonmaster.salonmaster.main;
    exports org.salonmaster.salonmaster.Controller;
}
