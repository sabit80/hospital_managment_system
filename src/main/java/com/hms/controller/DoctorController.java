package com.hms.controller;

import com.hms.model.Doctor;
import com.hms.service.DoctorService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class DoctorController {
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TextField searchField;
    @FXML private TableColumn<Doctor, Integer> idColumn;
    @FXML private TableColumn<Doctor, String> firstNameColumn;
    @FXML private TableColumn<Doctor, String> lastNameColumn;
    @FXML private TableColumn<Doctor, String> specializationColumn;
    @FXML private TableColumn<Doctor, String> phoneColumn;
    @FXML private TableColumn<Doctor, String> emailColumn;
    @FXML private TableColumn<Doctor, String> licenseColumn;
    @FXML private TableColumn<Doctor, String> descriptionColumn;
    
    private DoctorService doctorService;
    private StackPane parentContainer;

    @FXML
    public void initialize() {
        doctorService = new DoctorService();
        configureColumns();
        loadDoctors();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        licenseColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
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
        String searchTerm = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        doctorTable.getItems().clear();
        doctors.stream()
            .filter(d -> (d.getFirstName() != null && d.getFirstName().toLowerCase().contains(searchTerm)) ||
                        (d.getSpecialization() != null && d.getSpecialization().toLowerCase().contains(searchTerm)))
            .forEach(d -> doctorTable.getItems().add(d));
    }

    @FXML
    public void handleAddDoctor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-doctor.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Doctor");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadDoctors();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load add doctor form: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a doctor to edit");
            return;
        }
        showInfo("Edit Doctor", "Edit functionality - Coming soon for: " + selected.getFullName());
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
