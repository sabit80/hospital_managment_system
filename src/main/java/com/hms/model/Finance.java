package com.hms.model;

import java.time.LocalDate;

public class Finance {
    private int id;
    private LocalDate transactionDate;
    private String type; // Income, Expense
    private String category; // Patient Service, Equipment, Staff, etc.
    private String description;
    private double amount;
    private String paymentMethod;
    private String remarks;

    public Finance() {
        this.transactionDate = LocalDate.now();
    }

    public Finance(int id, LocalDate transactionDate, String type, String category,
                   String description, double amount, String paymentMethod) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.type = type;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
