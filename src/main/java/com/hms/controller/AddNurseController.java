package com.hms.controller;

import com.hms.model.Nurse;
import com.hms.service.NurseService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class AddNurseController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField licenseNumberField;
    @FXML private ComboBox<String> departmentCombo;
    @FXML private TextField floorField;
    @FXML private TextField roomNumberField;
    @FXML private TextField workingHoursField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    private final NurseService nurseService = new NurseService();
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @FXML
    public void initialize() {
        departmentCombo.getItems().addAll(
            "ICU",
            "Emergency",
            "Pediatrics",
            "Surgery",
            "Cardiology",
            "Oncology",
            "Maternity",
            "General Ward",
            "Orthopedics",
            "Neurology"
        );
    }

    @FXML
    private void handleSaveNurse() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String licenseNumber = licenseNumberField.getText().trim();
        String department = departmentCombo.getValue();
        String floor = floorField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        String workingHours = workingHoursField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || licenseNumber.isEmpty()) {
            showStatus("Please fill in all required fields (First Name, Last Name, License Number)", "error");
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
        
        // Parse license number
        int license;
        try {
            license = Integer.parseInt(licenseNumber);
        } catch (NumberFormatException e) {
            showStatus("License number must be a valid number", "error");
            return;
        }

        // Create nurse object
        Nurse nurse = new Nurse();
        nurse.setFirstName(firstName);
        nurse.setLastName(lastName);
        nurse.setPhone(phone);
        nurse.setEmail(email);
        nurse.setLicenseNumber(license);
        nurse.setDepartment(department != null ? department : "");
        nurse.setFloor(floor);
        nurse.setRoomNumber(roomNumber);
        nurse.setWorkingHours(workingHours);
        nurse.setDescription(description);

        // Save to database
        if (nurseService.addNurse(nurse)) {
            showStatus("✓ Nurse registered successfully!", "success");
            clearForm();
        } else {
            showStatus("Failed to register nurse. Please try again.", "error");
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
        licenseNumberField.clear();
        departmentCombo.setValue(null);
        floorField.clear();
        roomNumberField.clear();
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
