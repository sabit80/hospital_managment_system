package com.hms.controller;

import com.hms.model.Patient;
import com.hms.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class AddPatientController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> bloodGroupCombo;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextArea addressArea;
    @FXML private Label statusLabel;
    
    private PatientService patientService;

    @FXML
    public void initialize() {
        patientService = new PatientService();
        
        // Populate gender combo box
        genderCombo.getItems().addAll("Male", "Female", "Other");
        
        // Populate blood group combo box
        bloodGroupCombo.getItems().addAll("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
    }

    @FXML
    public void handleSavePatient() {
        // Validate input
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
            showError("Validation Error", "First name and last name are required!");
            return;
        }

        if (dateOfBirthPicker.getValue() == null) {
            showError("Validation Error", "Date of birth is required!");
            return;
        }

        // Create patient object
        Patient patient = new Patient();
        patient.setFirstName(firstNameField.getText());
        patient.setLastName(lastNameField.getText());
        patient.setDateOfBirth(dateOfBirthPicker.getValue());
        patient.setGender(genderCombo.getValue());
        patient.setBloodGroup(bloodGroupCombo.getValue());
        patient.setPhone(phoneField.getText());
        patient.setEmail(emailField.getText());
        patient.setAddress(addressArea.getText());
        patient.setRegistrationDate(LocalDate.now());

        // Save to database
        if (patientService.addPatient(patient)) {
            statusLabel.setText("✓ Patient registered successfully!");
            statusLabel.setStyle("-fx-text-fill: #4CAF50;");
            clearFields();
            
            // Auto-hide status after 3 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    statusLabel.setText("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showError("Error", "Failed to register patient!");
        }
    }

    @FXML
    public void handleCancel() {
        clearFields();
        statusLabel.setText("");
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        dateOfBirthPicker.setValue(null);
        genderCombo.setValue(null);
        bloodGroupCombo.setValue(null);
        phoneField.clear();
        emailField.clear();
        addressArea.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
