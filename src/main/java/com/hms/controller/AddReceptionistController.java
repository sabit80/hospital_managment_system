package com.hms.controller;

import com.hms.model.Reception;
import com.hms.service.ReceptionService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class AddReceptionistController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField employeeIdField;
    @FXML private TextField workingHoursField;
    @FXML private ComboBox<String> shiftCombo;
    @FXML private TextField departmentField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    private final ReceptionService receptionService = new ReceptionService();
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @FXML
    public void initialize() {
        shiftCombo.getItems().addAll(
            "Morning (6:00 AM - 2:00 PM)",
            "Afternoon (2:00 PM - 10:00 PM)",
            "Night (10:00 PM - 6:00 AM)",
            "Full Day (9:00 AM - 5:00 PM)"
        );
    }

    @FXML
    private void handleSaveReceptionist() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String employeeId = employeeIdField.getText().trim();
        String workingHours = workingHoursField.getText().trim();
        String shift = shiftCombo.getValue();
        String department = departmentField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || employeeId.isEmpty()) {
            showStatus("Please fill in all required fields (First Name, Last Name, Employee ID)", "error");
            return;
        }

        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Please enter a valid email address", "error");
            return;
        }

        if (!phone.isEmpty() && phone.length() < 6) {
            showStatus("Phone number must be at least 6 digits", "error");
            return;
        }

        // Create reception record for receptionist info (using the Reception model for staff storage)
        Reception receptionist = new Reception();
        receptionist.setVisitorName(firstName + " " + lastName);
        receptionist.setVisitorPhone(phone);
        receptionist.setReceptionistName(firstName + " " + lastName);
        receptionist.setRemarks("Receptionist - " + department + " | " + (shift != null ? shift : "") + " | Employee: " + employeeId + " | Hours: " + workingHours + " | " + description);
        receptionist.setStatus("Active");
        receptionist.setPurposeOfVisit("Receptionist - " + department);

        // Save to database (storing as reception record with special status)
        if (receptionService.addReception(receptionist)) {
            showStatus("✓ Receptionist registered successfully!", "success");
            clearForm();
        } else {
            showStatus("Failed to register receptionist. Please try again.", "error");
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
        employeeIdField.clear();
        workingHoursField.clear();
        shiftCombo.setValue(null);
        departmentField.clear();
        descriptionArea.clear();
        statusLabel.setText("");
    }

    private void showStatus(String message, String type) {
        statusLabel.setText(message);
        statusLabel.setStyle(type.equals("error") ? 
            "-fx-text-fill: #EF4444; -fx-font-weight: bold;" : 
            "-fx-text-fill: #10B981; -fx-font-weight: bold;");
        
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> statusLabel.setText(""));
        pause.play();
    }
}
