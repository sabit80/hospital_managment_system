package com.hms.controller;

import com.hms.App;
import com.hms.model.Appointment;
import com.hms.model.Doctor;
import com.hms.model.Operation;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.OperationService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DoctorDashboardController {

    @FXML
    private ComboBox<Doctor> doctorCombo;

    @FXML
    private Label appointmentCountLabel;

    @FXML
    private Label operationCountLabel;

    @FXML
    private VBox appointmentsBox;

    @FXML
    private VBox operationsBox;

    @FXML
    private Label statusLabel;

    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private OperationService operationService;

    @FXML
    public void initialize() {
        doctorService = new DoctorService();
        appointmentService = new AppointmentService();
        operationService = new OperationService();

        doctorCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });
        doctorCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName());
            }
        });

        List<Doctor> doctors = doctorService.getAllDoctors();
        doctorCombo.getItems().setAll(doctors);
        doctorCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadDoctorData(newValue);
            }
        });

        if (!doctors.isEmpty()) {
            doctorCombo.getSelectionModel().select(0);
        } else {
            statusLabel.setText("No doctors available. Add a doctor first.");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("views/role-selection");
    }

    private void loadDoctorData(Doctor doctor) {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndDate(doctor.getId(), today);
        List<Operation> operations = operationService.getOperationsByDoctorAndDate(doctor.getId(), today);

        appointmentCountLabel.setText(String.valueOf(appointments.size()));
        operationCountLabel.setText(String.valueOf(operations.size()));

        appointmentsBox.getChildren().clear();
        if (appointments.isEmpty()) {
            Label empty = new Label("No appointments for today.");
            empty.setStyle("-fx-text-fill: #64748B;");
            appointmentsBox.getChildren().add(empty);
        } else {
            for (Appointment appointment : appointments) {
                appointmentsBox.getChildren().add(buildRow(
                    appointment.getAppointmentTime(),
                    appointment.getPatientName(),
                    appointment.getStatus()
                ));
            }
        }

        operationsBox.getChildren().clear();
        if (operations.isEmpty()) {
            Label empty = new Label("No operations scheduled for today.");
            empty.setStyle("-fx-text-fill: #64748B;");
            operationsBox.getChildren().add(empty);
        } else {
            for (Operation operation : operations) {
                String time = operation.getOperationTime() == null ? "" : operation.getOperationTime();
                String patient = operation.getPatientName() == null ? "Patient" : operation.getPatientName();
                String status = operation.getStatus() == null ? "" : operation.getStatus();
                operationsBox.getChildren().add(buildRow(time, patient, status));
            }
        }

        statusLabel.setText("Schedule updated for " + doctor.getFullName());
    }

    private HBox buildRow(String time, String title, String status) {
        HBox row = new HBox(12);
        row.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 10 12; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10;");

        Label timeLabel = new Label(time == null ? "" : time);
        timeLabel.setStyle("-fx-font-weight: 600; -fx-text-fill: #0F172A; -fx-min-width: 90;");
        Label titleLabel = new Label(title == null ? "" : title);
        titleLabel.setStyle("-fx-text-fill: #0F172A;");
        Label statusLabel = new Label(status == null ? "" : status);
        statusLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: 600;");

        row.getChildren().addAll(timeLabel, titleLabel, statusLabel);
        return row;
    }
}
