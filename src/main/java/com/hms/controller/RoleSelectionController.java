package com.hms.controller;

import com.hms.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class RoleSelectionController {

    @FXML
    private void openManager() throws IOException {
        App.setRoot("views/manager-dashboard");
    }

    @FXML
    private void openDoctor() throws IOException {
        App.setRoot("views/doctor-dashboard");
    }

    @FXML
    private void openReceptionist() throws IOException {
        App.setRoot("views/receptionist-dashboard");
    }
}
