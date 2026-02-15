package com.hms.controller;

import com.hms.model.Cleaner;
import com.hms.service.CleanerService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class AddCleanerController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField assignedFloorField;
    @FXML private TextField assignedAreaField;
    @FXML private ComboBox<String> shiftCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField workingHoursField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    private final CleanerService cleanerService = new CleanerService();
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @FXML
    public void initialize() {
        shiftCombo.getItems().addAll(
            "Morning (6:00 AM - 2:00 PM)",
            "Afternoon (2:00 PM - 10:00 PM)",
            "Night (10:00 PM - 6:00 AM)"
        );
        statusCombo.getItems().addAll("Available", "In Use");
        statusCombo.setValue("Available");
    }

    @FXML
    private void handleSaveCleaner() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String assignedFloor = assignedFloorField.getText().trim();
        String assignedArea = assignedAreaField.getText().trim();
        String shift = shiftCombo.getValue();
        String status = statusCombo.getValue();
        String workingHours = workingHoursField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showStatus("Please fill in required fields (First Name, Last Name)", "error");
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

        // Create cleaner object
        Cleaner cleaner = new Cleaner();
        cleaner.setFirstName(firstName);
        cleaner.setLastName(lastName);
        cleaner.setPhone(phone);
        cleaner.setEmail(email);
        cleaner.setAssignedFloor(assignedFloor);
        cleaner.setAssignedArea(assignedArea);
        cleaner.setShift(shift != null ? shift : "");
        cleaner.setStatus(status == null || status.isBlank() ? "Available" : status);
        cleaner.setWorkingHours(workingHours);
        cleaner.setDescription(description);

        // Save to database
        if (cleanerService.addCleaner(cleaner)) {
            showStatus("✓ Cleaner registered successfully!", "success");
            clearForm();
        } else {
            showStatus("Failed to register cleaner. Please try again.", "error");
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
        assignedFloorField.clear();
        assignedAreaField.clear();
        shiftCombo.setValue(null);
        statusCombo.setValue("Available");
        workingHoursField.clear();
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
