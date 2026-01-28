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
        
        loadAmbulances();
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
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Ambulance");
        alert.setContentText("Edit functionality - Coming soon!");
        alert.showAndWait();
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
}
