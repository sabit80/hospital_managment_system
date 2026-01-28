package com.hms.controller;

import com.hms.model.Reception;
import com.hms.service.ReceptionService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReceptionController {
    @FXML private TableView<Reception> receptionTable;
    @FXML private TableColumn<Reception, Integer> idColumn;
    @FXML private TableColumn<Reception, String> visitorNameColumn;
    @FXML private TableColumn<Reception, String> patientNameColumn;
    @FXML private TableColumn<Reception, String> phoneColumn;
    @FXML private TableColumn<Reception, String> purposeColumn;
    @FXML private TableColumn<Reception, String> checkInColumn;
    @FXML private TableColumn<Reception, String> checkOutColumn;
    @FXML private TableColumn<Reception, String> statusColumn;
    @FXML private TextField searchField;
    
    private ReceptionService receptionService;
    private ObservableList<Reception> allReceptions = FXCollections.observableArrayList();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        receptionService = new ReceptionService();
        
        // Bind columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        visitorNameColumn.setCellValueFactory(new PropertyValueFactory<>("visitorName"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("visitorPhone"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purposeOfVisit"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Format datetime columns
        checkInColumn.setCellValueFactory(cellData -> {
            LocalDateTime dt = cellData.getValue().getCheckInTime();
            return new javafx.beans.property.SimpleStringProperty(dt != null ? dt.format(timeFormatter) : "");
        });
        
        checkOutColumn.setCellValueFactory(cellData -> {
            LocalDateTime dt = cellData.getValue().getCheckOutTime();
            return new javafx.beans.property.SimpleStringProperty(dt != null ? dt.format(timeFormatter) : "-");
        });
        
        loadReceptions();
    }

    @FXML
    public void loadReceptions() {
        List<Reception> receptions = receptionService.getAllReceptions();
        allReceptions.clear();
        allReceptions.addAll(receptions);
        receptionTable.setItems(allReceptions);
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            receptionTable.setItems(allReceptions);
            return;
        }
        
        ObservableList<Reception> filtered = allReceptions.stream()
            .filter(r -> r.getVisitorName().toLowerCase().contains(query) ||
                        (r.getPatientName() != null && r.getPatientName().toLowerCase().contains(query)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        
        receptionTable.setItems(filtered);
    }

    @FXML
    public void handleCheckIn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-receptionist.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add Receptionist / Check In Visitor");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadReceptions();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to open check-in form: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCheckOut() {
        Reception selected = receptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a visitor to check out");
            alert.showAndWait();
            return;
        }
        
        if (selected.getStatus() != null && selected.getStatus().equals("Checked Out")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Already Checked Out");
            alert.setContentText(selected.getVisitorName() + " is already checked out");
            alert.showAndWait();
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Check Out");
        confirmation.setHeaderText("Check out " + selected.getVisitorName() + "?");
        confirmation.setContentText("Check out time: " + LocalDateTime.now().format(timeFormatter));
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (receptionService.checkOut(selected.getId())) {
                    loadReceptions();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Visitor checked out successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to check out visitor");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    public void handleDeleteReception() {
        Reception selected = receptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a record to delete");
            alert.showAndWait();
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Record");
        confirmation.setHeaderText("Delete record for " + selected.getVisitorName() + "?");
        confirmation.setContentText("This action cannot be undone.");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (receptionService.deleteReception(selected.getId())) {
                    loadReceptions();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setContentText("Record deleted successfully");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to delete record");
                    error.showAndWait();
                }
            }
        });
    }
}
