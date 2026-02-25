module com.hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.hms to javafx.fxml;
    opens com.hms.controller to javafx.fxml;
    
    exports com.hms;
    exports com.hms.controller;
    exports com.hms.model;
    exports com.hms.service;
    exports com.hms.database;
}
