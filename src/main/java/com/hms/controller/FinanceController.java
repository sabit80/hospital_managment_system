package com.hms.controller;

import com.hms.model.Finance;
import com.hms.service.FinanceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class FinanceController {
    @FXML private TableView<Finance> financeTable;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label netProfitLabel;
    
    private FinanceService financeService;

    @FXML
    public void initialize() {
        financeService = new FinanceService();
        loadFinanceData();
        loadTransactions();
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
    public void handleAddIncome() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Income");
        alert.setHeaderText("Record new income");
        alert.setContentText("Income entry form would be displayed here");
        alert.showAndWait();
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
}
