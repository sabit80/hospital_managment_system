package com.hms.controller;

import com.hms.model.Doctor;
import com.hms.service.DoctorService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddDoctorController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ComboBox<String> specializationCombo;
    @FXML private TextField licenseNumberField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField workingHoursField;
    @FXML private TextField officeLocationField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;
    
    private DoctorService doctorService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @FXML
    public void initialize() {
        doctorService = new DoctorService();
        
        // Populate specialization combo box
        specializationCombo.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics",
            "General Surgery", "Dentistry", "Dermatology", "ENT"
        );
    }

    @FXML
    public void handleSaveDoctor() {
        // Validate input
        if (isBlank(firstNameField.getText()) || isBlank(lastNameField.getText())) {
            showError("Validation Error", "First name and last name are required!");
            return;
        }

        if (specializationCombo.getValue() == null) {
            showError("Validation Error", "Please select a specialization!");
            return;
        }

        if (isBlank(licenseNumberField.getText())) {
            showError("Validation Error", "License number is required!");
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

        // Create doctor object
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstNameField.getText().trim());
        doctor.setLastName(lastNameField.getText().trim());
        doctor.setSpecialization(specializationCombo.getValue());
        doctor.setLicenseNumber(licenseNumberField.getText().trim());
        doctor.setPhone(phoneField.getText() == null ? "" : phoneField.getText().trim());
        doctor.setEmail(emailField.getText() == null ? "" : emailField.getText().trim());
        doctor.setWorkingHours(workingHoursField.getText() == null ? "" : workingHoursField.getText().trim());
        doctor.setOfficeLocation(officeLocationField.getText() == null ? "" : officeLocationField.getText().trim());
        doctor.setDescription(descriptionArea.getText() == null ? "" : descriptionArea.getText().trim());
        doctor.setHireDate(LocalDate.now());

        // Save to database
        if (doctorService.addDoctor(doctor)) {
            statusLabel.setText("✓ Doctor saved successfully!");
            statusLabel.setStyle("-fx-text-fill: #4CAF50;");
            clearFields();
            
            // Auto-hide status after 3 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> statusLabel.setText(""));
            pause.play();
        } else {
            String dbError = doctorService.getLastError();
            if (dbError != null && dbError.contains("UNIQUE constraint failed: doctors.license_number")) {
                showError("Error", "License number already exists. Please use a unique license number.");
            } else {
                showError("Error", "Failed to save doctor!" + (dbError != null ? "\n" + dbError : ""));
            }
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
        specializationCombo.setValue(null);
        licenseNumberField.clear();
        phoneField.clear();
        emailField.clear();
        workingHoursField.clear();
        officeLocationField.clear();
        descriptionArea.clear();
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
