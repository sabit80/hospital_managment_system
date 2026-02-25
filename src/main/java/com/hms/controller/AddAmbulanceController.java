package com.hms.controller;

import com.hms.model.Ambulance;
import com.hms.service.AmbulanceService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDate;

public class AddAmbulanceController {
    @FXML private TextField vehicleNumberField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField driverNameField;
    @FXML private TextField driverPhoneField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField currentLocationField;
    @FXML private TextField capacityField;
    @FXML private DatePicker lastServiceDatePicker;
    @FXML private TextField fuelStatusField;
    @FXML private CheckBox equippedCheckbox;
    @FXML private TextField operationalCostField;
    @FXML private TextField serviceFeeField;
    @FXML private TextArea equipmentListArea;
    @FXML private Label statusLabel;

    private final AmbulanceService ambulanceService = new AmbulanceService();

    @FXML
    public void initialize() {
        typeCombo.getItems().addAll(
            "Basic Life Support (BLS)",
            "Advanced Life Support (ALS)",
            "Mobile ICU",
            "Air Ambulance",
            "Patient Transport"
        );
        
        statusCombo.getItems().addAll(
            "Available",
            "In Use",
            "Maintenance",
            "Out of Service"
        );
    }

    @FXML
    private void handleSaveAmbulance() {
        String vehicleNumber = vehicleNumberField.getText().trim();
        String type = typeCombo.getValue();
        String driverName = driverNameField.getText().trim();
        String driverPhone = driverPhoneField.getText().trim();
        String status = statusCombo.getValue();
        String currentLocation = currentLocationField.getText().trim();
        String capacityStr = capacityField.getText().trim();
        LocalDate lastServiceDate = lastServiceDatePicker.getValue();
        String fuelStatus = fuelStatusField.getText().trim();
        boolean equipped = equippedCheckbox.isSelected();
        String operationalCostText = operationalCostField.getText().trim();
        String serviceFeeText = serviceFeeField.getText().trim();
        String equipmentList = equipmentListArea.getText().trim();

        // Validation
        if (vehicleNumber.isEmpty() || driverName.isEmpty()) {
            showStatus("Please fill in required fields (Vehicle Number, Driver Name)", "error");
            return;
        }

        if (!driverPhone.isEmpty() && driverPhone.length() < 6) {
            showStatus("Driver phone must be at least 6 digits", "error");
            return;
        }
        
        // Parse capacity
        int capacity = 0;
        if (!capacityStr.isEmpty()) {
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                showStatus("Capacity must be a valid number", "error");
                return;
            }
        }

        Double operationalCost = parseAmountStrict(operationalCostText);
        if (operationalCost == null || operationalCost < 0) {
            showStatus("Operational cost must be a valid number", "error");
            return;
        }

        Double serviceFee = parseAmountStrict(serviceFeeText);
        if (serviceFee == null || serviceFee < 0) {
            showStatus("Service fee must be a valid number", "error");
            return;
        }

        // Create ambulance object
        Ambulance ambulance = new Ambulance();
        ambulance.setVehicleNumber(vehicleNumber);
        ambulance.setType(type != null ? type : "");
        ambulance.setDriverName(driverName);
        ambulance.setDriverPhone(driverPhone);
        ambulance.setStatus(status != null ? status : "Available");
        ambulance.setCurrentLocation(currentLocation);
        ambulance.setCapacity(capacity);
        if (lastServiceDate != null) {
            ambulance.setLastServiceDate(lastServiceDate.atStartOfDay());
        }
        ambulance.setFuelStatus(fuelStatus);
        ambulance.setEquipped(equipped);
        ambulance.setEquipmentList(equipmentList);
        ambulance.setOperationalCost(operationalCost);
        ambulance.setServiceFee(serviceFee);

        // Save to database
        if (ambulanceService.addAmbulance(ambulance)) {
            showStatus("✓ Ambulance registered successfully!", "success");
            clearForm();
        } else {
            showStatus("Failed to register ambulance. Please try again.", "error");
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        vehicleNumberField.clear();
        typeCombo.setValue(null);
        driverNameField.clear();
        driverPhoneField.clear();
        statusCombo.setValue(null);
        currentLocationField.clear();
        capacityField.clear();
        lastServiceDatePicker.setValue(null);
        fuelStatusField.clear();
        operationalCostField.clear();
        serviceFeeField.clear();
        equippedCheckbox.setSelected(false);
        equipmentListArea.clear();
        statusLabel.setText("");
    }

    private Double parseAmountStrict(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
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
