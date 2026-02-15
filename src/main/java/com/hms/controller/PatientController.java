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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

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
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    
    @FXML
    public void initialize() {
        patientService = new PatientService();
        setupTableColumns();
        setupRowHandlers();
        loadPatients();
    }

    private void setupRowHandlers() {
        patientsTable.setRowFactory(table -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showPatientProfile(row.getItem());
                }
            });
            return row;
        });
    }
    
    private void setupTableColumns() {
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    
    @FXML
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
            Scene scene = new Scene(addPatientView);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            stage.setScene(scene);
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

    @FXML
    private void handleEditPatient() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a patient to edit");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Patient");
        dialog.setHeaderText("Update patient details for " + selected.getFullName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField(selected.getFirstName());
        TextField lastNameField = new TextField(selected.getLastName());
        DatePicker dobPicker = new DatePicker(selected.getDateOfBirth());

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setValue(selected.getGender());

        ComboBox<String> bloodGroupCombo = new ComboBox<>();
        bloodGroupCombo.getItems().addAll("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
        bloodGroupCombo.setValue(selected.getBloodGroup());

        TextField phoneField = new TextField(selected.getPhone());
        TextField emailField = new TextField(selected.getEmail());
        TextArea addressArea = new TextArea(selected.getAddress());
        addressArea.setPrefRowCount(3);

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Date of Birth:"), 0, 2);
        grid.add(dobPicker, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(genderCombo, 1, 3);
        grid.add(new Label("Blood Group:"), 0, 4);
        grid.add(bloodGroupCombo, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("Email:"), 0, 6);
        grid.add(emailField, 1, 6);
        grid.add(new Label("Address:"), 0, 7);
        grid.add(addressArea, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (isBlank(firstNameField.getText()) || isBlank(lastNameField.getText())) {
                    showError("Validation Error", "First name and last name are required!");
                    return;
                }
                if (dobPicker.getValue() == null) {
                    showError("Validation Error", "Date of birth is required!");
                    return;
                }
                if (dobPicker.getValue().isAfter(LocalDate.now())) {
                    showError("Validation Error", "Date of birth cannot be in the future!");
                    return;
                }
                if (!isBlank(emailField.getText()) && !EMAIL_PATTERN.matcher(emailField.getText().trim()).matches()) {
                    showError("Validation Error", "Please enter a valid email address!");
                    return;
                }
                if (!isBlank(phoneField.getText()) && phoneField.getText().trim().length() < 6) {
                    showError("Validation Error", "Please enter a valid phone number!");
                    return;
                }

                Patient updated = new Patient();
                updated.setId(selected.getId());
                updated.setFirstName(firstNameField.getText().trim());
                updated.setLastName(lastNameField.getText().trim());
                updated.setDateOfBirth(dobPicker.getValue());
                updated.setGender(genderCombo.getValue());
                updated.setBloodGroup(bloodGroupCombo.getValue());
                updated.setPhone(phoneField.getText() == null ? "" : phoneField.getText().trim());
                updated.setEmail(emailField.getText() == null ? "" : emailField.getText().trim());
                updated.setAddress(addressArea.getText() == null ? "" : addressArea.getText().trim());
                updated.setRegistrationDate(selected.getRegistrationDate());

                if (patientService.updatePatient(updated)) {
                    loadPatients();
                    statusLabel.setText("Patient updated successfully");
                } else {
                    showError("Error", "Failed to update patient!");
                }
            }
        });
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showPatientProfile(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Profile");
        alert.setHeaderText(patient.getFullName());
        String details = String.join("\n",
            "Patient ID: " + patient.getId(),
            "Gender: " + safe(patient.getGender()),
            "Date of Birth: " + valueOrNA(patient.getDateOfBirth()),
            "Blood Group: " + safe(patient.getBloodGroup()),
            "Phone: " + safe(patient.getPhone()),
            "Email: " + safe(patient.getEmail()),
            "Address: " + safe(patient.getAddress()),
            "Registration Date: " + valueOrNA(patient.getRegistrationDate())
        );
        alert.setContentText(details);
        alert.showAndWait();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private String valueOrNA(Object value) {
        return value == null ? "N/A" : value.toString();
    }
}
