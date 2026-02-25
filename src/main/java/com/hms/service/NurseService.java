package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Nurse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NurseService {
    private Connection connection;

    public NurseService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addNurse(Nurse nurse) {
        String sql = """
            INSERT INTO nurses (first_name, last_name, phone, email, license_number, description,
                               hire_date, working_hours, floor, room_number, department, salary)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nurse.getFirstName());
            pstmt.setString(2, nurse.getLastName());
            pstmt.setString(3, nurse.getPhone());
            pstmt.setString(4, nurse.getEmail());
            pstmt.setInt(5, nurse.getLicenseNumber());
            pstmt.setString(6, nurse.getDescription());
            pstmt.setDate(7, Date.valueOf(nurse.getHireDate()));
            pstmt.setString(8, nurse.getWorkingHours());
            pstmt.setString(9, nurse.getFloor());
            pstmt.setString(10, nurse.getRoomNumber());
            pstmt.setString(11, nurse.getDepartment());
            pstmt.setDouble(12, nurse.getSalary());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding nurse: " + e.getMessage());
            return false;
        }
    }

    public List<Nurse> getAllNurses() {
        List<Nurse> nurses = new ArrayList<>();
        String sql = "SELECT * FROM nurses ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Nurse nurse = new Nurse();
                nurse.setId(rs.getInt("id"));
                nurse.setFirstName(rs.getString("first_name"));
                nurse.setLastName(rs.getString("last_name"));
                nurse.setPhone(rs.getString("phone"));
                nurse.setEmail(rs.getString("email"));
                nurse.setLicenseNumber(rs.getInt("license_number"));
                nurse.setDescription(rs.getString("description"));
                nurse.setHireDate(rs.getDate("hire_date").toLocalDate());
                nurse.setWorkingHours(rs.getString("working_hours"));
                nurse.setFloor(rs.getString("floor"));
                nurse.setRoomNumber(rs.getString("room_number"));
                nurse.setDepartment(rs.getString("department"));
                nurse.setSalary(rs.getDouble("salary"));
                
                nurses.add(nurse);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching nurses: " + e.getMessage());
        }
        
        return nurses;
    }

    public boolean updateNurse(Nurse nurse) {
        String sql = """
            UPDATE nurses SET first_name = ?, last_name = ?, phone = ?, email = ?, 
                             license_number = ?, description = ?, working_hours = ?,
                             floor = ?, room_number = ?, department = ?, salary = ? WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nurse.getFirstName());
            pstmt.setString(2, nurse.getLastName());
            pstmt.setString(3, nurse.getPhone());
            pstmt.setString(4, nurse.getEmail());
            pstmt.setInt(5, nurse.getLicenseNumber());
            pstmt.setString(6, nurse.getDescription());
            pstmt.setString(7, nurse.getWorkingHours());
            pstmt.setString(8, nurse.getFloor());
            pstmt.setString(9, nurse.getRoomNumber());
            pstmt.setString(10, nurse.getDepartment());
            pstmt.setDouble(11, nurse.getSalary());
            pstmt.setInt(12, nurse.getId());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating nurse: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteNurse(int id) {
        String sql = "DELETE FROM nurses WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting nurse: " + e.getMessage());
            return false;
        }
    }
}
