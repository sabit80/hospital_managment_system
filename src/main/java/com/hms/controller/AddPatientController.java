package com.hms.controller;

import com.hms.model.Patient;
import com.hms.service.PatientService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.time.LocalDate;
import java.util.regex.Pattern;

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
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

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
        if (isBlank(firstNameField.getText()) || isBlank(lastNameField.getText())) {
            showError("Validation Error", "First name and last name are required!");
            return;
        }

        if (dateOfBirthPicker.getValue() == null) {
            showError("Validation Error", "Date of birth is required!");
            return;
        }
        
        if (dateOfBirthPicker.getValue().isAfter(LocalDate.now())) {
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

        // Create patient object
        Patient patient = new Patient();
        patient.setFirstName(firstNameField.getText().trim());
        patient.setLastName(lastNameField.getText().trim());
        patient.setDateOfBirth(dateOfBirthPicker.getValue());
        patient.setGender(genderCombo.getValue());
        patient.setBloodGroup(bloodGroupCombo.getValue());
        patient.setPhone(phoneField.getText() == null ? "" : phoneField.getText().trim());
        patient.setEmail(emailField.getText() == null ? "" : emailField.getText().trim());
        patient.setAddress(addressArea.getText() == null ? "" : addressArea.getText().trim());
        patient.setRegistrationDate(LocalDate.now());

        // Save to database
        if (patientService.addPatient(patient)) {
            statusLabel.setText("✓ Patient registered successfully!");
            statusLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: 600;");
            clearFields();
            
            // Auto-hide status after 3 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> statusLabel.setText(""));
            pause.play();
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
}
