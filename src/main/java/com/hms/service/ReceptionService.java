package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Reception;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceptionService {
    private Connection connection;

    public ReceptionService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addReception(Reception reception) {
        String sql = """
            INSERT INTO reception (visitor_name, visitor_phone, patient_name, patient_id, 
                                  purpose_of_visit, check_in_time, receptionist_name, remarks, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reception.getVisitorName());
            pstmt.setString(2, reception.getVisitorPhone());
            pstmt.setString(3, reception.getPatientName());
            pstmt.setInt(4, reception.getPatientId());
            pstmt.setString(5, reception.getPurposeOfVisit());
            pstmt.setString(6, reception.getCheckInTime().toString());
            pstmt.setString(7, reception.getReceptionistName());
            pstmt.setString(8, reception.getRemarks());
            pstmt.setString(9, reception.getStatus());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding reception record: " + e.getMessage());
            return false;
        }
    }

    public List<Reception> getAllReceptions() {
        List<Reception> receptions = new ArrayList<>();
        String sql = "SELECT * FROM reception ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reception reception = new Reception();
                reception.setId(rs.getInt("id"));
                reception.setVisitorName(rs.getString("visitor_name"));
                reception.setVisitorPhone(rs.getString("visitor_phone"));
                reception.setPatientName(rs.getString("patient_name"));
                reception.setPatientId(rs.getInt("patient_id"));
                reception.setPurposeOfVisit(rs.getString("purpose_of_visit"));
                reception.setCheckInTime(LocalDateTime.parse(rs.getString("check_in_time")));
                String checkOut = rs.getString("check_out_time");
                if (checkOut != null) {
                    reception.setCheckOutTime(LocalDateTime.parse(checkOut));
                }
                reception.setReceptionistName(rs.getString("receptionist_name"));
                reception.setRemarks(rs.getString("remarks"));
                reception.setStatus(rs.getString("status"));
                
                receptions.add(reception);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching receptions: " + e.getMessage());
        }
        
        return receptions;
    }

    public boolean checkOut(int id) {
        String sql = "UPDATE reception SET check_out_time = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, LocalDateTime.now().toString());
            pstmt.setString(2, "Checked Out");
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error checking out: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteReception(int id) {
        String sql = "DELETE FROM reception WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting reception: " + e.getMessage());
            return false;
        }
    }
}
