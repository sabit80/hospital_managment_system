package com.hms.model;

import java.time.LocalDate;

public class FinanceDailySummary {
    private LocalDate date;
    private double income;
    private double expense;

    public FinanceDailySummary(LocalDate date, double income, double expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }
}
