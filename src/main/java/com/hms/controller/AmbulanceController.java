package com.hms.controller;

import com.hms.model.Ambulance;
import com.hms.service.AmbulanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class AmbulanceController {
    @FXML private TableView<Ambulance> ambulanceTable;
    @FXML private TableColumn<Ambulance, Integer> idColumn;
    @FXML private TableColumn<Ambulance, String> vehicleColumn;
    @FXML private TableColumn<Ambulance, String> typeColumn;
    @FXML private TableColumn<Ambulance, String> driverColumn;
    @FXML private TableColumn<Ambulance, String> phoneColumn;
    @FXML private TableColumn<Ambulance, String> statusColumn;
    @FXML private TableColumn<Ambulance, String> locationColumn;
    @FXML private TableColumn<Ambulance, Integer> capacityColumn;
    @FXML private TextField searchField;
    
    private AmbulanceService ambulanceService;
    private ObservableList<Ambulance> allAmbulances = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        ambulanceService = new AmbulanceService();
        
        // Bind columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("driverPhone"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("currentLocation"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        statusColumn.setCellFactory(column -> new TableCell<Ambulance, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(status);
                if ("Available".equalsIgnoreCase(status)) {
                    setStyle("-fx-text-fill: #10B981; -fx-font-weight: 600;");
                } else {
                    setStyle("-fx-text-fill: #EF4444; -fx-font-weight: 600;");
                }
            }
        });

        setupRowHandlers();
        
        loadAmbulances();
    }

    private void setupRowHandlers() {
        ambulanceTable.setRowFactory(table -> {
            TableRow<Ambulance> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showAmbulanceProfile(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    public void loadAmbulances() {
        List<Ambulance> ambulances = ambulanceService.getAllAmbulances();
        allAmbulances.clear();
        allAmbulances.addAll(ambulances);
        ambulanceTable.setItems(allAmbulances);
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            ambulanceTable.setItems(allAmbulances);
            return;
        }
        
        ObservableList<Ambulance> filtered = allAmbulances.stream()
            .filter(a -> a.getVehicleNumber().toLowerCase().contains(query) ||
                        (a.getDriverName() != null && a.getDriverName().toLowerCase().contains(query)) ||
                        (a.getStatus() != null && a.getStatus().toLowerCase().contains(query)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        
        ambulanceTable.setItems(filtered);
    }

    @FXML
    public void handleAddAmbulance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-ambulance.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Ambulance");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadAmbulances();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to open add ambulance form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditAmbulance() {
        Ambulance selected = ambulanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select an ambulance to edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Ambulance");
        dialog.setHeaderText("Update ambulance details for " + selected.getVehicleNumber());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField vehicleNumberField = new TextField(selected.getVehicleNumber());
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(
            "Basic Life Support (BLS)",
            "Advanced Life Support (ALS)",
            "Mobile ICU",
            "Air Ambulance",
            "Patient Transport"
        );
        typeCombo.setValue(selected.getType());

        TextField driverNameField = new TextField(selected.getDriverName());
        TextField driverPhoneField = new TextField(selected.getDriverPhone());
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Available", "In Use", "Maintenance", "Out of Service");
        statusCombo.setValue(selected.getStatus());
        TextField currentLocationField = new TextField(selected.getCurrentLocation());
        TextField capacityField = new TextField(String.valueOf(selected.getCapacity()));
        TextField fuelStatusField = new TextField(selected.getFuelStatus());
        CheckBox equippedCheckbox = new CheckBox("Equipped");
        equippedCheckbox.setSelected(selected.isEquipped());
        TextArea equipmentListArea = new TextArea(selected.getEquipmentList());
        equipmentListArea.setPrefRowCount(3);

        grid.add(new Label("Vehicle #:"), 0, 0);
        grid.add(vehicleNumberField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeCombo, 1, 1);
        grid.add(new Label("Driver Name:"), 0, 2);
        grid.add(driverNameField, 1, 2);
        grid.add(new Label("Driver Phone:"), 0, 3);
        grid.add(driverPhoneField, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusCombo, 1, 4);
        grid.add(new Label("Location:"), 0, 5);
        grid.add(currentLocationField, 1, 5);
        grid.add(new Label("Capacity:"), 0, 6);
        grid.add(capacityField, 1, 6);
        grid.add(new Label("Fuel Status:"), 0, 7);
        grid.add(fuelStatusField, 1, 7);
        grid.add(equippedCheckbox, 1, 8);
        grid.add(new Label("Equipment List:"), 0, 9);
        grid.add(equipmentListArea, 1, 9);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String vehicleNumber = vehicleNumberField.getText().trim();
                String driverName = driverNameField.getText().trim();
                String driverPhone = driverPhoneField.getText().trim();
                String capacityStr = capacityField.getText().trim();

                if (vehicleNumber.isEmpty() || driverName.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setContentText("Please fill in required fields (Vehicle Number, Driver Name)");
                    alert.showAndWait();
                    return;
                }

                if (!driverPhone.isEmpty() && driverPhone.length() < 6) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setContentText("Driver phone must be at least 6 digits");
                    alert.showAndWait();
                    return;
                }

                int capacity = 0;
                if (!capacityStr.isEmpty()) {
                    try {
                        capacity = Integer.parseInt(capacityStr);
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Validation Error");
                        alert.setContentText("Capacity must be a valid number");
                        alert.showAndWait();
                        return;
                    }
                }

                Ambulance updated = new Ambulance();
                updated.setId(selected.getId());
                updated.setVehicleNumber(vehicleNumber);
                updated.setType(typeCombo.getValue() != null ? typeCombo.getValue() : "");
                updated.setDriverName(driverName);
                updated.setDriverPhone(driverPhone);
                updated.setStatus(statusCombo.getValue() != null ? statusCombo.getValue() : "Available");
                updated.setCurrentLocation(currentLocationField.getText().trim());
                updated.setCapacity(capacity);
                updated.setEquipped(equippedCheckbox.isSelected());
                updated.setEquipmentList(equipmentListArea.getText() == null ? "" : equipmentListArea.getText().trim());
                updated.setFuelStatus(fuelStatusField.getText() == null ? "" : fuelStatusField.getText().trim());
                updated.setLastServiceDate(selected.getLastServiceDate());

                if (ambulanceService.updateAmbulance(updated)) {
                    loadAmbulances();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Ambulance updated successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to update ambulance");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    public void handleUpdateStatus() {
        Ambulance selected = ambulanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select an ambulance to update");
            alert.showAndWait();
            return;
        }
        
        // Create status update dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Ambulance Status");
        dialog.setHeaderText("Update status for " + selected.getVehicleNumber());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Available", "In Use", "Maintenance", "Out of Service");
        statusCombo.setValue(selected.getStatus());
        
        TextField locationField = new TextField();
        locationField.setText(selected.getCurrentLocation());
        locationField.setPromptText("Current location");
        
        grid.add(new Label("Status:"), 0, 0);
        grid.add(statusCombo, 1, 0);
        grid.add(new Label("Location:"), 0, 1);
        grid.add(locationField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String newStatus = statusCombo.getValue();
                String newLocation = locationField.getText().trim();
                
                if (ambulanceService.updateStatus(selected.getId(), newStatus, newLocation)) {
                    loadAmbulances();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Status updated successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to update status");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    public void handleDeleteAmbulance() {
        Ambulance selected = ambulanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select an ambulance to delete");
            alert.showAndWait();
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Ambulance");
        confirmation.setHeaderText("Delete ambulance " + selected.getVehicleNumber() + "?");
        confirmation.setContentText("This action cannot be undone.");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (ambulanceService.deleteAmbulance(selected.getId())) {
                    loadAmbulances();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Ambulance deleted successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete ambulance");
                    error.showAndWait();
                }
            }
        });
    }

    private void showAmbulanceProfile(Ambulance ambulance) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ambulance Profile");
        alert.setHeaderText(ambulance.getVehicleNumber());
        String details = String.join("\n",
            "Ambulance ID: " + ambulance.getId(),
            "Type: " + safe(ambulance.getType()),
            "Driver: " + safe(ambulance.getDriverName()),
            "Driver Phone: " + safe(ambulance.getDriverPhone()),
            "Status: " + safe(ambulance.getStatus()),
            "Location: " + safe(ambulance.getCurrentLocation()),
            "Capacity: " + ambulance.getCapacity(),
            "Equipped: " + (ambulance.isEquipped() ? "Yes" : "No"),
            "Fuel Status: " + safe(ambulance.getFuelStatus()),
            "Last Service: " + valueOrNA(ambulance.getLastServiceDate()),
            "Equipment List: " + safe(ambulance.getEquipmentList())
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
