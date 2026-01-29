package com.hms.controller;

import com.hms.database.DatabaseManager;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import com.hms.service.AmbulanceService;
import com.hms.service.NurseService;
import com.hms.service.CleanerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private Label totalAmbulancesLabel;
    
    @FXML
    private Label totalEmployeesLabel;
    
    @FXML
    private Label currentDateLabel;
    
    private PatientService patientService;
    private DoctorService doctorService;
    private AmbulanceService ambulanceService;
    private NurseService nurseService;
    private CleanerService cleanerService;

    @FXML
    public void initialize() {
        // Initialize database
        DatabaseManager.getInstance();
        patientService = new PatientService();
        doctorService = new DoctorService();
        ambulanceService = new AmbulanceService();
        nurseService = new NurseService();
        cleanerService = new CleanerService();
        updateDashboardStats();
        updateCurrentDate();
        statusLabel.setText("● Ready");
    }

    private void updateDashboardStats() {
        // Fetch real data from database
        int totalPatients = patientService.getAllPatients().size();
        int totalDoctors = doctorService.getAllDoctors().size();
        int totalAmbulances = ambulanceService.getAllAmbulances().size();
        int totalNurses = nurseService.getAllNurses().size();
        int totalCleaners = cleanerService.getAllCleaners().size();
        int totalEmployees = totalDoctors + totalNurses + totalCleaners;
        
        totalPatientsLabel.setText(String.valueOf(totalPatients));
        totalDoctorsLabel.setText(String.valueOf(totalDoctors));
        totalAmbulancesLabel.setText(String.valueOf(totalAmbulances));
        totalEmployeesLabel.setText(String.valueOf(totalEmployees));
    }
    
    private void updateCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        currentDateLabel.setText(LocalDate.now().format(formatter));
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
        loadView("add-appointment.fxml");
        statusLabel.setText("New Appointment");
    }

    @FXML
    private void showAppointmentList() {
        loadView("appointments.fxml");
        statusLabel.setText("Appointment List");
    }

    @FXML
    private void showDashboard() {
        try {
            // Reload the entire dashboard to show the welcome screen with stat cards
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/dashboard.fxml"));
            Parent dashboardView = loader.load();
            contentArea.getScene().setRoot(dashboardView);
        } catch (IOException e) {
            // If reloading fails, just clear the content area and update stats
            contentArea.getChildren().clear();
            updateDashboardStats();
            statusLabel.setText("Dashboard");
            e.printStackTrace();
        }
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
