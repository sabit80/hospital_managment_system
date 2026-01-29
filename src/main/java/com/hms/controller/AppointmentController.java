package com.hms.controller;

import com.hms.model.Appointment;
import com.hms.service.AppointmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

public class AppointmentController {
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> idColumn;
    @FXML private TableColumn<Appointment, String> patientColumn;
    @FXML private TableColumn<Appointment, String> doctorColumn;
    @FXML private TableColumn<Appointment, LocalDate> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;
    @FXML private TableColumn<Appointment, String> notesColumn;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private AppointmentService appointmentService;
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        appointmentService = new AppointmentService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        try {
            loadAppointments();
        } catch (Exception e) {
            showError("Error", "Failed to load appointments: " + e.getMessage());
        }
    }

    @FXML
    public void loadAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        allAppointments.setAll(appointments);
        appointmentTable.setItems(allAppointments);
        statusLabel.setText("Total Appointments: " + appointments.size());
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            appointmentTable.setItems(allAppointments);
            statusLabel.setText("Total Appointments: " + allAppointments.size());
            return;
        }

        ObservableList<Appointment> filtered = allAppointments.stream()
            .filter(a -> (a.getPatientName() != null && a.getPatientName().toLowerCase().contains(query)) ||
                         (a.getDoctorName() != null && a.getDoctorName().toLowerCase().contains(query)) ||
                         (a.getStatus() != null && a.getStatus().toLowerCase().contains(query)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));

        appointmentTable.setItems(filtered);
        statusLabel.setText("Found: " + filtered.size() + " appointment(s)");
    }

    @FXML
    public void handleDeleteAppointment() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select an appointment to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Appointment");
        confirm.setHeaderText("Delete appointment for " + selected.getPatientName() + "?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (appointmentService.deleteAppointment(selected.getId())) {
                    loadAppointments();
                } else {
                    showError("Error", "Failed to delete appointment");
                }
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
