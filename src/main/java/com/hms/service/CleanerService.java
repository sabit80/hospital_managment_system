package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Cleaner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CleanerService {
    private Connection connection;

    public CleanerService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addCleaner(Cleaner cleaner) {
        String sql = """
            INSERT INTO cleaners (first_name, last_name, phone, email, description,
                                 hire_date, working_hours, assigned_floor, assigned_area, shift, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getFirstName());
            pstmt.setString(2, cleaner.getLastName());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getEmail());
            pstmt.setString(5, cleaner.getDescription());
            pstmt.setDate(6, Date.valueOf(cleaner.getHireDate()));
            pstmt.setString(7, cleaner.getWorkingHours());
            pstmt.setString(8, cleaner.getAssignedFloor());
            pstmt.setString(9, cleaner.getAssignedArea());
            pstmt.setString(10, cleaner.getShift());
            pstmt.setString(11, cleaner.getStatus());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding cleaner: " + e.getMessage());
            return false;
        }
    }

    public List<Cleaner> getAllCleaners() {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cleaner cleaner = new Cleaner();
                cleaner.setId(rs.getInt("id"));
                cleaner.setFirstName(rs.getString("first_name"));
                cleaner.setLastName(rs.getString("last_name"));
                cleaner.setPhone(rs.getString("phone"));
                cleaner.setEmail(rs.getString("email"));
                cleaner.setDescription(rs.getString("description"));
                cleaner.setHireDate(rs.getDate("hire_date").toLocalDate());
                cleaner.setWorkingHours(rs.getString("working_hours"));
                cleaner.setAssignedFloor(rs.getString("assigned_floor"));
                cleaner.setAssignedArea(rs.getString("assigned_area"));
                cleaner.setShift(rs.getString("shift"));
                String status = rs.getString("status");
                cleaner.setStatus(status == null || status.isBlank() ? "Available" : status);
                
                cleaners.add(cleaner);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching cleaners: " + e.getMessage());
        }
        
        return cleaners;
    }

    public boolean updateCleaner(Cleaner cleaner) {
        String sql = """
            UPDATE cleaners SET first_name = ?, last_name = ?, phone = ?, email = ?, 
                               description = ?, working_hours = ?, assigned_floor = ?,
                               assigned_area = ?, shift = ?, status = ? WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cleaner.getFirstName());
            pstmt.setString(2, cleaner.getLastName());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getEmail());
            pstmt.setString(5, cleaner.getDescription());
            pstmt.setString(6, cleaner.getWorkingHours());
            pstmt.setString(7, cleaner.getAssignedFloor());
            pstmt.setString(8, cleaner.getAssignedArea());
            pstmt.setString(9, cleaner.getShift());
            pstmt.setString(10, cleaner.getStatus());
            pstmt.setInt(11, cleaner.getId());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating cleaner: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCleaner(int id) {
        String sql = "DELETE FROM cleaners WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting cleaner: " + e.getMessage());
            return false;
        }
    }
}
