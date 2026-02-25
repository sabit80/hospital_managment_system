package com.hms.controller;

import com.hms.model.Cleaner;
import com.hms.service.CleanerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class CleanerController {
    @FXML private TableView<Cleaner> cleanerTable;
    @FXML private TableColumn<Cleaner, Integer> idColumn;
    @FXML private TableColumn<Cleaner, String> firstNameColumn;
    @FXML private TableColumn<Cleaner, String> lastNameColumn;
    @FXML private TableColumn<Cleaner, String> phoneColumn;
    @FXML private TableColumn<Cleaner, String> floorColumn;
    @FXML private TableColumn<Cleaner, String> areaColumn;
    @FXML private TableColumn<Cleaner, String> shiftColumn;
    @FXML private TableColumn<Cleaner, String> statusColumn;
    @FXML private TableColumn<Cleaner, String> descriptionColumn;
    @FXML private TextField searchField;
    
    private CleanerService cleanerService;
    private ObservableList<Cleaner> allCleaners = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cleanerService = new CleanerService();
        
        // Bind columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("assignedFloor"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("assignedArea"));
        shiftColumn.setCellValueFactory(new PropertyValueFactory<>("shift"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        statusColumn.setCellFactory(column -> new TableCell<Cleaner, String>() {
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
        
        loadCleaners();
    }

    private void setupRowHandlers() {
        cleanerTable.setRowFactory(table -> {
            TableRow<Cleaner> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showCleanerProfile(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    public void loadCleaners() {
        List<Cleaner> cleaners = cleanerService.getAllCleaners();
        allCleaners.clear();
        allCleaners.addAll(cleaners);
        cleanerTable.setItems(allCleaners);
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            cleanerTable.setItems(allCleaners);
            return;
        }
        
        ObservableList<Cleaner> filtered = allCleaners.stream()
            .filter(c -> c.getFirstName().toLowerCase().contains(query) ||
                        c.getLastName().toLowerCase().contains(query) ||
                        (c.getAssignedFloor() != null && c.getAssignedFloor().toLowerCase().contains(query)) ||
                        (c.getAssignedArea() != null && c.getAssignedArea().toLowerCase().contains(query)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        
        cleanerTable.setItems(filtered);
    }

    @FXML
    public void handleAddCleaner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-cleaner.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Cleaner");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadCleaners();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to open add cleaner form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditCleaner() {
        Cleaner selected = cleanerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a cleaner to edit");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Cleaner");
        alert.setContentText("Edit functionality - Coming soon!");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteCleaner() {
        Cleaner selected = cleanerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a cleaner to delete");
            alert.showAndWait();
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Cleaner");
        confirmation.setHeaderText("Delete " + selected.getFullName() + "?");
        confirmation.setContentText("This action cannot be undone.");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (cleanerService.deleteCleaner(selected.getId())) {
                    loadCleaners();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Cleaner deleted successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete cleaner");
                    error.showAndWait();
                }
            }
        });
    }

    private void showCleanerProfile(Cleaner cleaner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cleaner Profile");
        alert.setHeaderText(cleaner.getFullName());
        String details = String.join("\n",
            "Cleaner ID: " + cleaner.getId(),
            "Phone: " + safe(cleaner.getPhone()),
            "Email: " + safe(cleaner.getEmail()),
            "Assigned Floor: " + safe(cleaner.getAssignedFloor()),
            "Assigned Area: " + safe(cleaner.getAssignedArea()),
            "Shift: " + safe(cleaner.getShift()),
            "Working Hours: " + safe(cleaner.getWorkingHours()),
            "Hire Date: " + valueOrNA(cleaner.getHireDate()),
            "Salary: " + String.format("%.2f", cleaner.getSalary()),
            "Description: " + safe(cleaner.getDescription())
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
