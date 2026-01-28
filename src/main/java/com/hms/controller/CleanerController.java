package com.hms.controller;

import com.hms.model.Cleaner;
import com.hms.service.CleanerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class CleanerController {
    @FXML private TableView<Cleaner> cleanerTable;
    @FXML private TextField searchField;
    
    private CleanerService cleanerService;

    @FXML
    public void initialize() {
        cleanerService = new CleanerService();
        loadCleaners();
    }

    @FXML
    public void loadCleaners() {
        List<Cleaner> cleaners = cleanerService.getAllCleaners();
        cleanerTable.getItems().clear();
        cleanerTable.getItems().addAll(cleaners);
    }

    @FXML
    public void handleSearch() {
        // Search functionality
    }

    @FXML
    public void handleAddCleaner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Cleaner");
        alert.setHeaderText("Add new cleaner");
        alert.setContentText("Cleaner registration form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleEditCleaner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Cleaner");
        alert.setHeaderText("Edit cleaner details");
        alert.setContentText("Edit form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteCleaner() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Cleaner");
        alert.setHeaderText("Delete cleaner?");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
    }
}
