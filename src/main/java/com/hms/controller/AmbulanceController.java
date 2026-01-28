package com.hms.controller;

import com.hms.model.Ambulance;
import com.hms.service.AmbulanceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class AmbulanceController {
    @FXML private TableView<Ambulance> ambulanceTable;
    @FXML private TextField searchField;
    
    private AmbulanceService ambulanceService;

    @FXML
    public void initialize() {
        ambulanceService = new AmbulanceService();
        loadAmbulances();
    }

    @FXML
    public void loadAmbulances() {
        List<Ambulance> ambulances = ambulanceService.getAllAmbulances();
        ambulanceTable.getItems().clear();
        ambulanceTable.getItems().addAll(ambulances);
    }

    @FXML
    public void handleSearch() {
        // Search functionality
    }

    @FXML
    public void handleAddAmbulance() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Ambulance");
        alert.setHeaderText("Add new ambulance");
        alert.setContentText("Ambulance registration form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleEditAmbulance() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Ambulance");
        alert.setHeaderText("Edit ambulance details");
        alert.setContentText("Edit form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleUpdateStatus() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Status");
        alert.setHeaderText("Update ambulance status");
        alert.setContentText("Status update form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteAmbulance() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Ambulance");
        alert.setHeaderText("Delete ambulance?");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
    }
}
