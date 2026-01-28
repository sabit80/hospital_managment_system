package com.hms.controller;

import com.hms.model.Nurse;
import com.hms.service.NurseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class NurseController {
    @FXML private TableView<Nurse> nurseTable;
    @FXML private TextField searchField;
    
    private NurseService nurseService;

    @FXML
    public void initialize() {
        nurseService = new NurseService();
        loadNurses();
    }

    @FXML
    public void loadNurses() {
        List<Nurse> nurses = nurseService.getAllNurses();
        nurseTable.getItems().clear();
        nurseTable.getItems().addAll(nurses);
    }

    @FXML
    public void handleSearch() {
        // Search functionality
    }

    @FXML
    public void handleAddNurse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Nurse");
        alert.setHeaderText("Add new nurse");
        alert.setContentText("Nurse registration form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleEditNurse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Nurse");
        alert.setHeaderText("Edit nurse details");
        alert.setContentText("Edit form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteNurse() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Nurse");
        alert.setHeaderText("Delete nurse?");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
    }
}
