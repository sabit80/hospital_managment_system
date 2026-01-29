package com.hms.controller;

import com.hms.model.Doctor;
import com.hms.service.DoctorService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class DoctorController {
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TextField searchField;
    @FXML private TableColumn<Doctor, Integer> idColumn;
    @FXML private TableColumn<Doctor, String> firstNameColumn;
    @FXML private TableColumn<Doctor, String> lastNameColumn;
    @FXML private TableColumn<Doctor, String> specializationColumn;
    @FXML private TableColumn<Doctor, String> phoneColumn;
    @FXML private TableColumn<Doctor, String> emailColumn;
    @FXML private TableColumn<Doctor, String> licenseColumn;
    @FXML private TableColumn<Doctor, String> descriptionColumn;
    
    private DoctorService doctorService;
    private StackPane parentContainer;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @FXML
    public void initialize() {
        doctorService = new DoctorService();
        configureColumns();
        loadDoctors();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        licenseColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public void setParentContainer(StackPane container) {
        this.parentContainer = container;
    }

    @FXML
    public void loadDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        doctorTable.getItems().clear();
        doctorTable.getItems().addAll(doctors);
    }

    @FXML
    public void handleSearch() {
        String searchTerm = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        doctorTable.getItems().clear();
        doctors.stream()
            .filter(d -> (d.getFirstName() != null && d.getFirstName().toLowerCase().contains(searchTerm)) ||
                        (d.getSpecialization() != null && d.getSpecialization().toLowerCase().contains(searchTerm)))
            .forEach(d -> doctorTable.getItems().add(d));
    }

    @FXML
    public void handleAddDoctor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/add-doctor.fxml"));
            Parent view = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Doctor");
            
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/com/hms/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.showAndWait();
            
            loadDoctors();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "Could not load add doctor form: " + e.getMessage());
        }
    }

    @FXML
    public void handleEditDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a doctor to edit");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Doctor");
        dialog.setHeaderText("Update doctor details for " + selected.getFullName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField(selected.getFirstName());
        TextField lastNameField = new TextField(selected.getLastName());
        ComboBox<String> specializationCombo = new ComboBox<>();
        specializationCombo.getItems().addAll(
            "Cardiology", "Neurology", "Orthopedics", "Pediatrics",
            "General Surgery", "Dentistry", "Dermatology", "ENT"
        );
        specializationCombo.setValue(selected.getSpecialization());

        TextField licenseField = new TextField(selected.getLicenseNumber());
        TextField phoneField = new TextField(selected.getPhone());
        TextField emailField = new TextField(selected.getEmail());
        TextField workingHoursField = new TextField(selected.getWorkingHours());
        TextField officeLocationField = new TextField(selected.getOfficeLocation());
        TextArea descriptionArea = new TextArea(selected.getDescription());
        descriptionArea.setPrefRowCount(3);

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Specialization:"), 0, 2);
        grid.add(specializationCombo, 1, 2);
        grid.add(new Label("License #:"), 0, 3);
        grid.add(licenseField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Working Hours:"), 0, 6);
        grid.add(workingHoursField, 1, 6);
        grid.add(new Label("Office Location:"), 0, 7);
        grid.add(officeLocationField, 1, 7);
        grid.add(new Label("Description:"), 0, 8);
        grid.add(descriptionArea, 1, 8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (isBlank(firstNameField.getText()) || isBlank(lastNameField.getText())) {
                    showError("Validation Error", "First name and last name are required!");
                    return;
                }
                if (specializationCombo.getValue() == null) {
                    showError("Validation Error", "Please select a specialization!");
                    return;
                }
                if (isBlank(licenseField.getText())) {
                    showError("Validation Error", "License number is required!");
                    return;
                }
                if (!isBlank(emailField.getText()) && !EMAIL_PATTERN.matcher(emailField.getText().trim()).matches()) {
                    showError("Validation Error", "Please enter a valid email address!");
                    return;
                }
                if (!isBlank(phoneField.getText()) && phoneField.getText().trim().length() < 6) {
                    showError("Validation Error", "Please enter a valid phone number!");
                    return;
                }

                Doctor updated = new Doctor();
                updated.setId(selected.getId());
                updated.setFirstName(firstNameField.getText().trim());
                updated.setLastName(lastNameField.getText().trim());
                updated.setSpecialization(specializationCombo.getValue());
                updated.setLicenseNumber(licenseField.getText().trim());
                updated.setPhone(phoneField.getText() == null ? "" : phoneField.getText().trim());
                updated.setEmail(emailField.getText() == null ? "" : emailField.getText().trim());
                updated.setWorkingHours(workingHoursField.getText() == null ? "" : workingHoursField.getText().trim());
                updated.setOfficeLocation(officeLocationField.getText() == null ? "" : officeLocationField.getText().trim());
                updated.setDescription(descriptionArea.getText() == null ? "" : descriptionArea.getText().trim());
                updated.setHireDate(selected.getHireDate());

                if (doctorService.updateDoctor(updated)) {
                    loadDoctors();
                    showInfo("Success", "Doctor updated successfully");
                } else {
                    showError("Error", "Failed to update doctor");
                }
            }
        });
    }

    @FXML
    public void handleDeleteDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a doctor to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete doctor: " + selected.getFullName() + "?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            if (doctorService.deleteDoctor(selected.getId())) {
                loadDoctors();
                showInfo("Success", "Doctor deleted successfully");
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
