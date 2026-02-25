package com.hms.controller;

import com.hms.App;
import com.hms.database.DatabaseManager;
import com.hms.model.Appointment;
import com.hms.model.Ambulance;
import com.hms.model.Cleaner;
import com.hms.model.FinanceDailySummary;
import com.hms.model.Nurse;
import com.hms.service.AmbulanceService;
import com.hms.service.AppointmentService;
import com.hms.service.CleanerService;
import com.hms.service.DoctorService;
import com.hms.service.FinanceService;
import com.hms.service.NurseService;
import com.hms.service.PatientService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class ManagerDashboardController {

    @FXML
    private LineChart<String, Number> financeChart;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalExpenseLabel;

    @FXML
    private Label netProfitLabel;

    @FXML
    private Label reportRangeLabel;

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

    @FXML
    private VBox todayAppointmentsBox;

    @FXML
    private Label todayAppointmentsCountLabel;

    private PatientService patientService;
    private DoctorService doctorService;
    private AmbulanceService ambulanceService;
    private NurseService nurseService;
    private CleanerService cleanerService;
    private AppointmentService appointmentService;
    private FinanceService financeService;

    @FXML
    public void initialize() {
        DatabaseManager.getInstance();
        patientService = new PatientService();
        doctorService = new DoctorService();
        ambulanceService = new AmbulanceService();
        nurseService = new NurseService();
        cleanerService = new CleanerService();
        appointmentService = new AppointmentService();
        financeService = new FinanceService();

        updateDashboardStats();
        loadTodayAppointments();
        updateCurrentDate();
        loadReport();
        statusLabel.setText("● Ready");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("views/role-selection");
    }

    @FXML
    private void handleExportPdf() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        List<FinanceDailySummary> summaries = financeService.getDailySummary(startDate, endDate);

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save 30-Day Finance Report");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        chooser.setInitialFileName("manager-report-30-days.pdf");
        File file = chooser.showSaveDialog(financeChart.getScene().getWindow());
        if (file == null) {
            return;
        }

        if (writePdfReport(file, startDate, endDate, summaries)) {
            statusLabel.setText("● Report saved: " + file.getName());
        } else {
            statusLabel.setText("● Failed to export report.");
        }
    }

    private void loadReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        List<FinanceDailySummary> summaries = financeService.getDailySummary(startDate, endDate);
        Map<LocalDate, FinanceDailySummary> map = new HashMap<>();
        for (FinanceDailySummary summary : summaries) {
            map.put(summary.getDate(), summary);
        }

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM d");
        double totalIncome = 0;
        double totalExpense = 0;

        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            FinanceDailySummary summary = map.get(date);
            double income = summary == null ? 0 : summary.getIncome();
            double expense = summary == null ? 0 : summary.getExpense();
            totalIncome += income;
            totalExpense += expense;
            String label = date.format(labelFormatter);
            revenueSeries.getData().add(new XYChart.Data<>(label, income));
            expenseSeries.getData().add(new XYChart.Data<>(label, expense));
        }

        financeChart.setData(FXCollections.observableArrayList(revenueSeries, expenseSeries));
        totalRevenueLabel.setText(formatCurrency(totalIncome));
        totalExpenseLabel.setText(formatCurrency(totalExpense));
        netProfitLabel.setText(formatCurrency(totalIncome - totalExpense));
        reportRangeLabel.setText("Last 30 days • " + formatDate(startDate) + " - " + formatDate(endDate));
    }

    private void updateDashboardStats() {
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

    private void loadTodayAppointments() {
        if (todayAppointmentsBox == null) {
            return;
        }

        todayAppointmentsBox.getChildren().clear();
        List<Appointment> today = appointmentService.getAppointmentsByDate(LocalDate.now());
        todayAppointmentsCountLabel.setText(String.valueOf(today.size()));

        if (today.isEmpty()) {
            Label empty = new Label("No appointments for today.");
            empty.setStyle("-fx-text-fill: #64748B;");
            todayAppointmentsBox.getChildren().add(empty);
            return;
        }

        for (Appointment appointment : today) {
            HBox row = new HBox(12);
            row.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 10 12; -fx-background-radius: 10; -fx-border-color: #E5E7EB; -fx-border-radius: 10;");

            String time = appointment.getAppointmentTime() == null ? "" : appointment.getAppointmentTime();
            String patient = appointment.getPatientName() == null ? "Patient" : appointment.getPatientName();
            String doctor = appointment.getDoctorName() == null ? "Doctor" : appointment.getDoctorName();
            String status = appointment.getStatus() == null ? "" : appointment.getStatus();

            Label timeLabel = new Label(time);
            timeLabel.setStyle("-fx-font-weight: 600; -fx-text-fill: #0F172A; -fx-min-width: 90;");
            Label patientLabel = new Label(patient);
            patientLabel.setStyle("-fx-text-fill: #0F172A;");
            Label doctorLabel = new Label("• " + doctor);
            doctorLabel.setStyle("-fx-text-fill: #64748B;");
            Label statusTextLabel = new Label(status);
            statusTextLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: 600;");

            row.getChildren().addAll(timeLabel, patientLabel, doctorLabel, statusTextLabel);
            todayAppointmentsBox.getChildren().add(row);
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hms/views/manager-dashboard.fxml"));
            Parent dashboardView = loader.load();
            contentArea.getScene().setRoot(dashboardView);
        } catch (IOException e) {
            contentArea.getChildren().clear();
            updateDashboardStats();
            loadTodayAppointments();
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean writePdfReport(File file, LocalDate startDate, LocalDate endDate, List<FinanceDailySummary> summaries) {
        try (PDDocument document = new PDDocument()) {
            List<Appointment> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
            List<Nurse> nurses = nurseService.getAllNurses();
            List<Cleaner> cleaners = cleanerService.getAllCleaners();
            List<Ambulance> ambulances = ambulanceService.getAllAmbulances();

            writeDiagnosisPages(document, startDate, endDate, appointments);
            writeStaffSalaryPages(document, startDate, endDate, nurses, cleaners);
            writeAmbulancePages(document, startDate, endDate, ambulances);

            document.save(file);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing PDF: " + e.getMessage());
            return false;
        }
    }

    private void writeDiagnosisPages(PDDocument document, LocalDate startDate, LocalDate endDate,
                                    List<Appointment> appointments) throws IOException {
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        float y = 750;

        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 16, 50, y, "Diagnosis Report");
        y = writeLine(content, PDType1Font.HELVETICA, 11, 50, y - 12,
            "Range: " + formatDate(startDate) + " - " + formatDate(endDate));
        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 20,
            "Date        Time   Patient           Doctor            Diagnosis");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        for (Appointment appointment : appointments) {
            if (y < 80) {
                content.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 12, 50, y, "Diagnosis Report (cont.)");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 16,
                    "Date        Time   Patient           Doctor            Diagnosis");
            }

            String date = appointment.getAppointmentDate() == null
                ? "N/A"
                : appointment.getAppointmentDate().format(dateFormatter);
            String time = safe(appointment.getAppointmentTime());
            String patient = truncate(safe(appointment.getPatientName()), 16);
            String doctor = truncate(safe(appointment.getDoctorName()), 16);
            String diagnosis = truncate(safe(appointment.getNotes()), 40);

            String row = String.format("%-12s %-6s %-16s %-16s %s", date, time, patient, doctor, diagnosis);
            y = writeLine(content, PDType1Font.HELVETICA, 9, 50, y - 12, row);
        }

        if (appointments.isEmpty()) {
            writeLine(content, PDType1Font.HELVETICA, 10, 50, y - 14, "No diagnosis records in this range.");
        }

        content.close();
    }

    private void writeStaffSalaryPages(PDDocument document, LocalDate startDate, LocalDate endDate,
                                       List<Nurse> nurses, List<Cleaner> cleaners) throws IOException {
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        float y = 750;

        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 16, 50, y, "Nurse and Cleaner Salaries");
        y = writeLine(content, PDType1Font.HELVETICA, 11, 50, y - 12,
            "Range: " + formatDate(startDate) + " - " + formatDate(endDate));

        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 11, 50, y - 20, "Nurses");
        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 14,
            "Name                  Department          Salary");

        double nurseTotal = 0;
        for (Nurse nurse : nurses) {
            if (y < 80) {
                content.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 12, 50, y, "Nurse and Cleaner Salaries (cont.)");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 11, 50, y - 16, "Nurses");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 14,
                    "Name                  Department          Salary");
            }

            nurseTotal += nurse.getSalary();
            String row = String.format("%-22s %-18s %10.2f",
                truncate(nurse.getFullName(), 22),
                truncate(safe(nurse.getDepartment()), 18),
                nurse.getSalary());
            y = writeLine(content, PDType1Font.HELVETICA, 9, 50, y - 12, row);
        }

        if (nurses.isEmpty()) {
            y = writeLine(content, PDType1Font.HELVETICA, 10, 50, y - 12, "No nurses found.");
        } else {
            y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 12,
                "Total Nurse Salary: " + formatCurrency(nurseTotal));
        }

        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 11, 50, y - 18, "Cleaners");
        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 14,
            "Name                  Assignment          Salary");

        double cleanerTotal = 0;
        for (Cleaner cleaner : cleaners) {
            if (y < 80) {
                content.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 12, 50, y, "Nurse and Cleaner Salaries (cont.)");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 11, 50, y - 16, "Cleaners");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 14,
                    "Name                  Assignment          Salary");
            }

            cleanerTotal += cleaner.getSalary();
            String row = String.format("%-22s %-18s %10.2f",
                truncate(cleaner.getFullName(), 22),
                truncate(safe(cleaner.getAssignment()), 18),
                cleaner.getSalary());
            y = writeLine(content, PDType1Font.HELVETICA, 9, 50, y - 12, row);
        }

        if (cleaners.isEmpty()) {
            writeLine(content, PDType1Font.HELVETICA, 10, 50, y - 12, "No cleaners found.");
        } else {
            writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 12,
                "Total Cleaner Salary: " + formatCurrency(cleanerTotal));
        }

        content.close();
    }

    private void writeAmbulancePages(PDDocument document, LocalDate startDate, LocalDate endDate,
                                     List<Ambulance> ambulances) throws IOException {
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        float y = 750;

        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 16, 50, y, "Ambulance Cost and Fee Income");
        y = writeLine(content, PDType1Font.HELVETICA, 11, 50, y - 12,
            "Range: " + formatDate(startDate) + " - " + formatDate(endDate));
        y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 20,
            "Vehicle #        Type                Cost        Fee");

        double totalCost = 0;
        double totalFee = 0;
        for (Ambulance ambulance : ambulances) {
            if (y < 80) {
                content.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                content = new PDPageContentStream(document, page);
                y = 750;
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 12, 50, y, "Ambulance Cost and Fee Income (cont.)");
                y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 16,
                    "Vehicle #        Type                Cost        Fee");
            }

            totalCost += ambulance.getOperationalCost();
            totalFee += ambulance.getServiceFee();
            String row = String.format("%-16s %-18s %10.2f %10.2f",
                truncate(safe(ambulance.getVehicleNumber()), 16),
                truncate(safe(ambulance.getType()), 18),
                ambulance.getOperationalCost(),
                ambulance.getServiceFee());
            y = writeLine(content, PDType1Font.HELVETICA, 9, 50, y - 12, row);
        }

        if (ambulances.isEmpty()) {
            writeLine(content, PDType1Font.HELVETICA, 10, 50, y - 12, "No ambulances found.");
        } else {
            y = writeLine(content, PDType1Font.HELVETICA_BOLD, 10, 50, y - 12,
                "Total Cost: " + formatCurrency(totalCost) + "   Total Fee Income: " + formatCurrency(totalFee));
        }

        content.close();
    }

    private float writeLine(PDPageContentStream content, PDType1Font font, int size, float x, float y, String text)
            throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
        return y;
    }

    private String formatCurrency(double value) {
        return String.format("%,.2f", value);
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
