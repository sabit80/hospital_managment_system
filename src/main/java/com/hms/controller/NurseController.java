package com.hms.controller;

import com.hms.model.Nurse;
import com.hms.service.NurseService;
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

public class NurseController {
    @FXML private TableView<Nurse> nurseTable;
    @FXML private TableColumn<Nurse, Integer> idColumn;
    @FXML private TableColumn<Nurse, String> firstNameColumn;
    @FXML private TableColumn<Nurse, String> lastNameColumn;
    @FXML private TableColumn<Nurse, String> phoneColumn;
    @FXML private TableColumn<Nurse, String> floorColumn;
    @FXML private TableColumn<Nurse, String> roomColumn;
    @FXML private TableColumn<Nurse, String> departmentColumn;
    @FXML private TableColumn<Nurse, String> descriptionColumn;
    @FXML private TextField searchField;
    
    private NurseService nurseService;
    private ObservableList<Nurse> allNurses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nurseService = new NurseService();
        
        // Bind columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        setupRowHandlers();
        
        loadNurses();
    }

    private void setupRowHandlers() {
        nurseTable.setRowFactory(table -> {
            TableRow<Nurse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showNurseProfile(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    public void loadNurses() {
        List<Nurse> nurses = nurseService.getAllNurses();
        allNurses.clear();
        allNurses.addAll(nurses);
        nurseTable.setItems(allNurses);
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            nurseTable.setItems(allNurses);
            return;
        }
        
        ObservableList<Nurse> filtered = allNurses.stream()
            .filter(n -> n.getFirstName().toLowerCase().contains(query) ||
                        n.getLastName().toLowerCase().contains(query) ||
                        (n.getDepartment() != null && n.getDepartment().toLowerCase().contains(query)) ||
                        (n.getFloor() != null && n.getFloor().toLowerCase().contains(query)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        
        nurseTable.setItems(filtered);
    }

    @FXML
    public void handleAddNurse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-nurse.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Nurse");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadNurses();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to open add nurse form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditNurse() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a nurse to edit");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Nurse");
        alert.setContentText("Edit functionality - Coming soon!");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteNurse() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a nurse to delete");
            alert.showAndWait();
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Nurse");
        confirmation.setHeaderText("Delete " + selected.getFullName() + "?");
        confirmation.setContentText("This action cannot be undone.");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (nurseService.deleteNurse(selected.getId())) {
                    loadNurses();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Nurse deleted successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete nurse");
                    error.showAndWait();
                }
            }
        });
    }

    private void showNurseProfile(Nurse nurse) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nurse Profile");
        alert.setHeaderText(nurse.getFullName());
        String details = String.join("\n",
            "Nurse ID: " + nurse.getId(),
            "Phone: " + safe(nurse.getPhone()),
            "Email: " + safe(nurse.getEmail()),
            "License #: " + nurse.getLicenseNumber(),
            "Department: " + safe(nurse.getDepartment()),
            "Floor: " + safe(nurse.getFloor()),
            "Room: " + safe(nurse.getRoomNumber()),
            "Working Hours: " + safe(nurse.getWorkingHours()),
            "Hire Date: " + valueOrNA(nurse.getHireDate()),
            "Description: " + safe(nurse.getDescription())
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
