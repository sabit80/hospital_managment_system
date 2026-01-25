module com.hms {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hms to javafx.fxml;
    exports com.hms;
}
