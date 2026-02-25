package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Finance;
import com.hms.model.FinanceDailySummary;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceService {
    private Connection connection;

    public FinanceService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addTransaction(Finance finance) {
        String sql = """
            INSERT INTO finance (transaction_date, type, category, description, amount, payment_method, remarks)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(finance.getTransactionDate()));
            pstmt.setString(2, finance.getType());
            pstmt.setString(3, finance.getCategory());
            pstmt.setString(4, finance.getDescription());
            pstmt.setDouble(5, finance.getAmount());
            pstmt.setString(6, finance.getPaymentMethod());
            pstmt.setString(7, finance.getRemarks());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            return false;
        }
    }

    public List<Finance> getAllTransactions() {
        List<Finance> transactions = new ArrayList<>();
        String sql = "SELECT * FROM finance ORDER BY transaction_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Finance finance = new Finance();
                finance.setId(rs.getInt("id"));
                finance.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                finance.setType(rs.getString("type"));
                finance.setCategory(rs.getString("category"));
                finance.setDescription(rs.getString("description"));
                finance.setAmount(rs.getDouble("amount"));
                finance.setPaymentMethod(rs.getString("payment_method"));
                finance.setRemarks(rs.getString("remarks"));
                
                transactions.add(finance);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        
        return transactions;
    }

    public double getTotalIncome() {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM finance WHERE type = 'Income'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating income: " + e.getMessage());
        }
        
        return 0;
    }

    public double getTotalExpense() {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM finance WHERE type = 'Expense'";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating expense: " + e.getMessage());
        }
        
        return 0;
    }

    public double getProfit() {
        return getTotalIncome() - getTotalExpense();
    }

    public List<Finance> getTransactionsByType(String type) {
        List<Finance> transactions = new ArrayList<>();
        String sql = "SELECT * FROM finance WHERE type = ? ORDER BY transaction_date DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Finance finance = new Finance();
                finance.setId(rs.getInt("id"));
                finance.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                finance.setType(rs.getString("type"));
                finance.setCategory(rs.getString("category"));
                finance.setDescription(rs.getString("description"));
                finance.setAmount(rs.getDouble("amount"));
                finance.setPaymentMethod(rs.getString("payment_method"));
                finance.setRemarks(rs.getString("remarks"));
                
                transactions.add(finance);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        
        return transactions;
    }

    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM finance WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }

    public List<FinanceDailySummary> getDailySummary(LocalDate startDate, LocalDate endDate) {
        List<FinanceDailySummary> summaries = new ArrayList<>();
        String sql = """
            SELECT transaction_date,
                   SUM(CASE WHEN type = 'Income' THEN amount ELSE 0 END) AS income,
                   SUM(CASE WHEN type = 'Expense' THEN amount ELSE 0 END) AS expense
            FROM finance
            WHERE transaction_date BETWEEN ? AND ?
            GROUP BY transaction_date
            ORDER BY transaction_date ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("transaction_date").toLocalDate();
                double income = rs.getDouble("income");
                double expense = rs.getDouble("expense");
                summaries.add(new FinanceDailySummary(date, income, expense));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching daily summary: " + e.getMessage());
        }

        return summaries;
    }
}
