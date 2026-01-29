package com.hms.controller;

import com.hms.model.Appointment;
import com.hms.model.Doctor;
import com.hms.model.Patient;
import com.hms.service.AppointmentService;
import com.hms.service.DoctorService;
import com.hms.service.PatientService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

public class AddAppointmentController {
    @FXML private ComboBox<Patient> patientCombo;
    @FXML private ComboBox<Doctor> doctorCombo;
    @FXML private RadioButton todayRadio;
    @FXML private RadioButton tomorrowRadio;
    @FXML private RadioButton nextDayRadio;
    @FXML private TextField timeField;
    @FXML private TextArea notesArea;
    @FXML private Label statusLabel;

    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;

    @FXML
    public void initialize() {
        appointmentService = new AppointmentService();
        patientService = new PatientService();
        doctorService = new DoctorService();

        ToggleGroup dateGroup = new ToggleGroup();
        todayRadio.setToggleGroup(dateGroup);
        tomorrowRadio.setToggleGroup(dateGroup);
        nextDayRadio.setToggleGroup(dateGroup);
        todayRadio.setSelected(true);

        timeField.setText("10:00 AM");

        List<Patient> patients = patientService.getAllPatients();
        patientCombo.getItems().setAll(patients);
        patientCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName() + " (ID: " + item.getId() + ")");
            }
        });
        patientCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFullName() + " (ID: " + item.getId() + ")");
            }
        });

        List<Doctor> doctors = doctorService.getAllDoctors();
        doctorCombo.getItems().setAll(doctors);
        doctorCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                String specialization = item.getSpecialization() == null ? "" : item.getSpecialization();
                String label = item.getFullName() + (specialization.isBlank() ? "" : " - " + specialization);
                setText(label);
            }
        });
        doctorCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                String specialization = item.getSpecialization() == null ? "" : item.getSpecialization();
                String label = item.getFullName() + (specialization.isBlank() ? "" : " - " + specialization);
                setText(label);
            }
        });
    }

    @FXML
    private void handleSaveAppointment() {
        Patient patient = patientCombo.getValue();
        Doctor doctor = doctorCombo.getValue();

        if (patient == null || doctor == null) {
            showStatus("Please select both patient and doctor.", true);
            return;
        }

        LocalDate appointmentDate = LocalDate.now();
        if (tomorrowRadio.isSelected()) {
            appointmentDate = appointmentDate.plusDays(1);
        } else if (nextDayRadio.isSelected()) {
            appointmentDate = appointmentDate.plusDays(2);
        }

        String time = timeField.getText() == null ? "" : timeField.getText().trim();
        if (time.isEmpty()) {
            time = "10:00 AM";
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getId());
        appointment.setDoctorId(doctor.getId());
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(time);
        appointment.setStatus("Scheduled");
        appointment.setNotes(notesArea.getText() == null ? "" : notesArea.getText().trim());

        if (appointmentService.addAppointment(appointment)) {
            showStatus("✓ Appointment scheduled successfully!", false);
            clearForm();
        } else {
            showStatus("Failed to schedule appointment. Please try again.", true);
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        patientCombo.setValue(null);
        doctorCombo.setValue(null);
        todayRadio.setSelected(true);
        timeField.setText("10:00 AM");
        notesArea.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError
            ? "-fx-text-fill: #EF4444; -fx-font-weight: bold;"
            : "-fx-text-fill: #10B981; -fx-font-weight: bold;");

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> statusLabel.setText(""));
        pause.play();
    }
}
