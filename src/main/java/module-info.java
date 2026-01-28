module com.hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.hms to javafx.fxml;
    opens com.hms.controller to javafx.fxml;
    opens com.hms.views to javafx.fxml;
    
    exports com.hms;
    exports com.hms.controller;
    exports com.hms.model;
}
