package com.hms.controller;

import com.hms.model.Doctor;
import com.hms.service.DoctorService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

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
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()) {
            showError("Validation Error", "First name and last name are required!");
            return;
        }

        if (specializationCombo.getValue() == null) {
            showError("Validation Error", "Please select a specialization!");
            return;
        }

        if (licenseNumberField.getText().isEmpty()) {
            showError("Validation Error", "License number is required!");
            return;
        }

        // Create doctor object
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstNameField.getText());
        doctor.setLastName(lastNameField.getText());
        doctor.setSpecialization(specializationCombo.getValue());
        doctor.setLicenseNumber(licenseNumberField.getText());
        doctor.setPhone(phoneField.getText());
        doctor.setEmail(emailField.getText());
        doctor.setWorkingHours(workingHoursField.getText());
        doctor.setOfficeLocation(officeLocationField.getText());
        doctor.setDescription(descriptionArea.getText());
        doctor.setHireDate(LocalDate.now());

        // Save to database
        if (doctorService.addDoctor(doctor)) {
            statusLabel.setText("✓ Doctor saved successfully!");
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
            showError("Error", "Failed to save doctor!");
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
