package com.hms.controller;

import com.hms.model.Patient;
import com.hms.service.PatientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PatientController {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Patient> patientsTable;
    
    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;
    @FXML
    private TableColumn<Patient, String> firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn;
    @FXML
    private TableColumn<Patient, String> dateOfBirthColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, String> bloodGroupColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, String> actionsColumn;
    
    @FXML
    private Label statusLabel;
    
    private PatientService patientService;
    
    @FXML
    public void initialize() {
        patientService = new PatientService();
        setupTableColumns();
        loadPatients();
    }
    
    private void setupTableColumns() {
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    
    private void loadPatients() {
        List<Patient> patients = patientService.getAllPatients();
        patientsTable.setItems(FXCollections.observableArrayList(patients));
        statusLabel.setText("Total Patients: " + patients.size());
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadPatients();
            return;
        }
        
        List<Patient> allPatients = patientService.getAllPatients();
        List<Patient> filtered = allPatients.stream()
            .filter(p -> p.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        p.getLastName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        p.getPhone().contains(searchTerm))
            .toList();
        
        patientsTable.setItems(FXCollections.observableArrayList(filtered));
        statusLabel.setText("Found: " + filtered.size() + " patient(s)");
    }
    
    @FXML
    private void handleAddPatient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-patient.fxml"));
            Parent addPatientView = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Patient");
            stage.setScene(new Scene(addPatientView));
            stage.setWidth(500);
            stage.setHeight(600);
            stage.showAndWait();
            
            // Reload patient list after adding
            loadPatients();
        } catch (IOException e) {
            showError("Error", "Could not load add patient form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
