package com.hms.controller;

import com.hms.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label totalPatientsLabel;
    
    @FXML
    private Label totalDoctorsLabel;
    
    @FXML
    private Label todayAppointmentsLabel;
    
    @FXML
    private Label pendingAppointmentsLabel;

    @FXML
    public void initialize() {
        // Initialize database
        DatabaseManager.getInstance();
        updateDashboardStats();
        statusLabel.setText("System ready");
    }

    private void updateDashboardStats() {
        // TODO: Fetch real data from database
        totalPatientsLabel.setText("0");
        totalDoctorsLabel.setText("0");
        todayAppointmentsLabel.setText("0");
        pendingAppointmentsLabel.setText("0");
    }

    @FXML
    private void showPatientRegistration() {
        loadView("add-patient.fxml");
        statusLabel.setText("Add New Patient");
    }

    @FXML
    private void showPatientList() {
        loadView("patient-list.fxml");
        statusLabel.setText("Patient List");
    }

    @FXML
    private void showDoctorRegistration() {
        loadView("add-doctor.fxml");
        statusLabel.setText("Add New Doctor");
    }

    @FXML
    private void showDoctorList() {
        loadView("doctors.fxml");
        statusLabel.setText("Doctor Management");
    }

    @FXML
    private void showNurseList() {
        loadView("nurses.fxml");
        statusLabel.setText("Nurse Management");
    }

    @FXML
    private void showCleanerList() {
        loadView("cleaners.fxml");
        statusLabel.setText("Cleaner Management");
    }

    @FXML
    private void showReception() {
        loadView("reception.fxml");
        statusLabel.setText("Reception Management");
    }

    @FXML
    private void showAmbulances() {
        loadView("ambulances.fxml");
        statusLabel.setText("Ambulance Management");
    }

    @FXML
    private void showFinance() {
        loadView("finance.fxml");
        statusLabel.setText("Profit Management");
    }

    @FXML
    private void showAppointmentForm() {
        showInfo("Coming Soon", "Appointment booking feature will be available soon!");
    }

    @FXML
    private void showAppointmentList() {
        showInfo("Coming Soon", "Appointment list feature will be available soon!");
    }

    @FXML
    private void showDashboard() {
        contentArea.getChildren().clear();
        updateDashboardStats();
        statusLabel.setText("Dashboard");
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Hospital Management System");
        alert.setContentText("Version 1.0\nDeveloped with JavaFX and SQLite\n\n© 2026 All rights reserved");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        DatabaseManager.getInstance().close();
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            showError("Error", "Could not load view: " + fxmlFile);
            e.printStackTrace();
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
