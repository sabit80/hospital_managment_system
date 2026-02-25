package com.hms.controller;

import com.hms.App;
import com.hms.model.Bed;
import com.hms.model.Patient;
import com.hms.service.BedService;
import com.hms.service.PatientService;
import com.hms.service.WardService;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReceptionistDashboardController {

    @FXML
    private Label vacantWardLabel;

    @FXML
    private Label vacantIcuLabel;

    @FXML
    private Label vacantCcuLabel;

    @FXML
    private ComboBox<Patient> patientCombo;

    @FXML
    private ComboBox<Bed> bedCombo;

    @FXML
    private Label assignStatusLabel;

    @FXML
    private ListView<String> availableBedsList;

    private BedService bedService;
    private PatientService patientService;
    private WardService wardService;

    @FXML
    public void initialize() {
        bedService = new BedService();
        patientService = new PatientService();
        wardService = new WardService();

        wardService.ensureDefaultWardsAndBeds();
        configurePatientCombo();
        configureBedCombo();

        refreshBeds();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("views/role-selection");
    }

    @FXML
    private void handleAssignBed() {
        Patient patient = patientCombo.getValue();
        Bed bed = bedCombo.getValue();

        if (patient == null || bed == null) {
            assignStatusLabel.setText("Select both a patient and a bed.");
            assignStatusLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-weight: 600;");
            return;
        }

        if (bedService.assignBedToPatient(bed.getId(), patient.getId())) {
            assignStatusLabel.setText("Assigned bed to " + patient.getFullName());
            assignStatusLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: 600;");
            refreshBeds();
        } else {
            assignStatusLabel.setText("Failed to assign bed.");
            assignStatusLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-weight: 600;");
        }
    }

    @FXML
    private void openAppointmentForm() throws IOException {
        openModal("add-appointment.fxml", "New Appointment");
    }

    @FXML
    private void openAppointmentList() throws IOException {
        openModal("appointments.fxml", "Appointments");
    }

    private void configurePatientCombo() {
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
    }

    private void configureBedCombo() {
        bedCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Bed item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
        bedCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Bed item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
    }

    private void refreshBeds() {
        vacantWardLabel.setText(String.valueOf(bedService.getVacantCountByType("Ward")));
        vacantIcuLabel.setText(String.valueOf(bedService.getVacantCountByType("ICU")));
        vacantCcuLabel.setText(String.valueOf(bedService.getVacantCountByType("CCU")));

        List<Bed> availableBeds = bedService.getAvailableBeds();
        bedCombo.getItems().setAll(availableBeds);

        List<String> bedLines = availableBeds.stream()
            .map(Bed::getDisplayName)
            .toList();
        availableBedsList.setItems(FXCollections.observableArrayList(bedLines));

        if (availableBeds.isEmpty()) {
            assignStatusLabel.setText("No vacant beds available.");
            assignStatusLabel.setStyle("-fx-text-fill: #64748B;");
        } else {
            assignStatusLabel.setText("");
        }
    }

    private void openModal(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/" + fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        refreshBeds();
    }
}
