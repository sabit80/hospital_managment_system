package com.hms.controller;

import com.hms.model.Reception;
import com.hms.service.ReceptionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class ReceptionController {
    @FXML private TableView<Reception> receptionTable;
    @FXML private TextField searchField;
    
    private ReceptionService receptionService;

    @FXML
    public void initialize() {
        receptionService = new ReceptionService();
        loadReceptions();
    }

    @FXML
    public void loadReceptions() {
        List<Reception> receptions = receptionService.getAllReceptions();
        receptionTable.getItems().clear();
        receptionTable.getItems().addAll(receptions);
    }

    @FXML
    public void handleSearch() {
        // Search functionality
    }

    @FXML
    public void handleCheckIn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check In");
        alert.setHeaderText("New visitor check-in");
        alert.setContentText("Check-in form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleCheckOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check Out");
        alert.setHeaderText("Check out visitor");
        alert.setContentText("Check-out confirmation would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteReception() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Record");
        alert.setHeaderText("Delete reception record?");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
    }
}
