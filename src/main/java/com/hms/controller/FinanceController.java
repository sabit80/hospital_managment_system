package com.hms.controller;

import com.hms.model.Finance;
import com.hms.service.FinanceService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class FinanceController {
    @FXML private TableView<Finance> financeTable;
    @FXML private TableColumn<Finance, Integer> idColumn;
    @FXML private TableColumn<Finance, java.time.LocalDate> dateColumn;
    @FXML private TableColumn<Finance, String> typeColumn;
    @FXML private TableColumn<Finance, String> categoryColumn;
    @FXML private TableColumn<Finance, String> descriptionColumn;
    @FXML private TableColumn<Finance, Double> amountColumn;
    @FXML private TableColumn<Finance, String> methodColumn;
    @FXML private TableColumn<Finance, String> remarksColumn;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label netProfitLabel;
    
    private FinanceService financeService;

    @FXML
    public void initialize() {
        financeService = new FinanceService();
        configureColumns();
        loadFinanceData();
        loadTransactions();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<>("remarks"));
    }

    @FXML
    public void loadFinanceData() {
        double income = financeService.getTotalIncome();
        double expense = financeService.getTotalExpense();
        double profit = financeService.getProfit();
        
        totalIncomeLabel.setText(String.format("%.2f", income));
        totalExpenseLabel.setText(String.format("%.2f", expense));
        netProfitLabel.setText(String.format("%.2f", profit));
    }

    @FXML
    public void loadTransactions() {
        List<Finance> transactions = financeService.getAllTransactions();
        financeTable.getItems().clear();
        financeTable.getItems().addAll(transactions);
    }

    @FXML
    public void handleAddBill() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Patient Bill");
        dialog.setHeaderText("Appointment fee + medical services");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");

        TextField appointmentFeeField = new TextField();
        appointmentFeeField.setPromptText("0.00");

        ComboBox<String> paymentMethodCombo = new ComboBox<>();
        paymentMethodCombo.getItems().addAll("Cash", "Card", "Mobile Banking", "Insurance", "Other");
        paymentMethodCombo.setValue("Cash");

        TextArea remarksArea = new TextArea();
        remarksArea.setPrefRowCount(2);

        grid.add(new Label("Patient ID:"), 0, 0);
        grid.add(patientIdField, 1, 0);
        grid.add(new Label("Appointment Fee:"), 0, 1);
        grid.add(appointmentFeeField, 1, 1);
        grid.add(new Label("Payment Method:"), 0, 2);
        grid.add(paymentMethodCombo, 1, 2);

        Label extrasLabel = new Label("Additional Services:");
        extrasLabel.setStyle("-fx-font-weight: 600;");
        grid.add(extrasLabel, 0, 3);

        VBox extrasBox = new VBox(6);
        List<BillItem> billItems = new ArrayList<>();
        billItems.add(new BillItem("ICU"));
        billItems.add(new BillItem("Bed Fee"));
        billItems.add(new BillItem("Lab Tests"));
        billItems.add(new BillItem("Medicine"));
        billItems.add(new BillItem("Surgery"));
        BillItem otherItem = new BillItem("Other", true);
        billItems.add(otherItem);

        for (BillItem item : billItems) {
            extrasBox.getChildren().add(item.getRow());
        }

        grid.add(extrasBox, 1, 3);

        Label totalLabel = new Label("Total: 0.00");
        totalLabel.setStyle("-fx-font-weight: 700;");
        grid.add(totalLabel, 1, 4);

        grid.add(new Label("Remarks:"), 0, 5);
        grid.add(remarksArea, 1, 5);

        Runnable updateTotal = () -> {
            double appointmentFee = parseAmount(appointmentFeeField.getText());
            double extras = billItems.stream()
                .filter(BillItem::isSelected)
                .mapToDouble(item -> parseAmount(item.getAmountText()))
                .sum();
            totalLabel.setText(String.format("Total: %.2f", appointmentFee + extras));
        };

        appointmentFeeField.textProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());
        for (BillItem item : billItems) {
            item.addListeners(updateTotal);
        }
        updateTotal.run();

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Integer patientId = parseInt(patientIdField.getText());
                if (patientId == null || patientId <= 0) {
                    showError("Validation Error", "Please enter a valid patient ID.");
                    return;
                }

                Double appointmentFee = parseAmountStrict(appointmentFeeField.getText());
                if (appointmentFee == null || appointmentFee < 0) {
                    showError("Validation Error", "Appointment fee must be a valid number.");
                    return;
                }

                for (BillItem item : billItems) {
                    if (!item.isSelected()) {
                        continue;
                    }
                    if (item.isOther() && (item.getOtherName() == null || item.getOtherName().isBlank())) {
                        showError("Validation Error", "Please enter a name for the Other service.");
                        return;
                    }
                    Double amount = parseAmountStrict(item.getAmountText());
                    if (amount == null || amount < 0) {
                        showError("Validation Error", "Please enter valid amounts for selected services.");
                        return;
                    }
                }

                String description = buildBillDescription(patientId, appointmentFee, billItems);
                double totalAmount = parseAmount(appointmentFeeField.getText()) + billItems.stream()
                    .filter(BillItem::isSelected)
                    .mapToDouble(item -> parseAmount(item.getAmountText()))
                    .sum();

                Finance finance = new Finance();
                finance.setType("Income");
                finance.setCategory("Patient Bill");
                finance.setDescription(description);
                finance.setAmount(totalAmount);
                finance.setPaymentMethod(paymentMethodCombo.getValue());
                finance.setRemarks(remarksArea.getText() == null ? "" : remarksArea.getText().trim());

                if (financeService.addTransaction(finance)) {
                    loadFinanceData();
                    loadTransactions();
                    showInfo("Success", "Patient bill added.");
                } else {
                    showError("Error", "Failed to add bill.");
                }
            }
        });
    }

    @FXML
    public void handleAddExpense() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Expense");
        alert.setHeaderText("Record new expense");
        alert.setContentText("Expense entry form would be displayed here");
        alert.showAndWait();
    }

    @FXML
    public void handleDeleteTransaction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Delete transaction?");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
    }

    @FXML
    public void handleExportReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Report");
        alert.setHeaderText("Export financial report");
        alert.setContentText("Report would be exported as PDF/Excel");
        alert.showAndWait();
    }

    private String buildBillDescription(int patientId, double appointmentFee, List<BillItem> billItems) {
        StringBuilder builder = new StringBuilder();
        builder.append("Patient ID: ").append(patientId);
        builder.append(" | Appointment Fee: ").append(String.format("%.2f", appointmentFee));
        for (BillItem item : billItems) {
            if (!item.isSelected()) {
                continue;
            }
            String name = item.isOther() ? item.getOtherName() : item.getName();
            builder.append(" | ").append(name).append(": ").append(String.format("%.2f", parseAmount(item.getAmountText())));
        }
        return builder.toString();
    }

    private Double parseAmountStrict(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseAmount(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Integer parseInt(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return null;
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

    private static class BillItem {
        private final String name;
        private final boolean other;
        private final CheckBox checkBox;
        private final TextField amountField;
        private final TextField otherNameField;
        private final HBox row;

        BillItem(String name) {
            this(name, false);
        }

        BillItem(String name, boolean other) {
            this.name = name;
            this.other = other;
            this.checkBox = new CheckBox(name);
            this.amountField = new TextField();
            this.amountField.setPromptText("0.00");
            this.amountField.setPrefWidth(100);
            this.otherNameField = other ? new TextField() : null;
            if (otherNameField != null) {
                otherNameField.setPromptText("Service name");
                otherNameField.setPrefWidth(140);
            }
            this.row = new HBox(8);
            if (otherNameField != null) {
                row.getChildren().addAll(checkBox, otherNameField, amountField);
            } else {
                row.getChildren().addAll(checkBox, amountField);
            }
        }

        HBox getRow() {
            return row;
        }

        String getName() {
            return name;
        }

        boolean isSelected() {
            return checkBox.isSelected();
        }

        String getAmountText() {
            return amountField.getText();
        }

        boolean isOther() {
            return other;
        }

        String getOtherName() {
            return otherNameField == null ? null : otherNameField.getText();
        }

        void addListeners(Runnable updateTotal) {
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());
            amountField.textProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());
            if (otherNameField != null) {
                otherNameField.textProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());
            }
        }
    }
}
