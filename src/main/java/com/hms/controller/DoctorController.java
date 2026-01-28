package com.hms.controller;

import com.hms.model.Doctor;
import com.hms.service.DoctorService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.List;

public class DoctorController {
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TextField searchField;
    
    private DoctorService doctorService;
    private StackPane parentContainer;

    @FXML
    public void initialize() {
        doctorService = new DoctorService();
        loadDoctors();
    }

    public void setParentContainer(StackPane container) {
        this.parentContainer = container;
    }

    @FXML
    public void loadDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        doctorTable.getItems().clear();
        doctorTable.getItems().addAll(doctors);
    }

    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        doctorTable.getItems().clear();
        doctors.stream()
            .filter(d -> d.getFirstName().toLowerCase().contains(searchTerm) || 
                        d.getSpecialization().toLowerCase().contains(searchTerm))
            .forEach(d -> doctorTable.getItems().add(d));
    }

    @FXML
    public void handleAddDoctor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-doctor.fxml"));
            Parent view = loader.load();
            if (parentContainer != null) {
                parentContainer.getChildren().clear();
                parentContainer.getChildren().add(view);
            }
        } catch (IOException e) {
            showError("Error", "Could not load add doctor form: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a doctor to edit");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Doctor");
        alert.setHeaderText("Edit doctor details");
        alert.setContentText("Edit form would be displayed here for: " + selected.getFullName());
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a doctor to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete doctor: " + selected.getFullName() + "?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            if (doctorService.deleteDoctor(selected.getId())) {
                loadDoctors();
                showInfo("Success", "Doctor deleted successfully");
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
