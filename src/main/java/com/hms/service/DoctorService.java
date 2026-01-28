package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Doctor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private Connection connection;

    public DoctorService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addDoctor(Doctor doctor) {
        String sql = """
            INSERT INTO doctors (first_name, last_name, specialization, phone, email, 
                                license_number, description, hire_date, working_hours, office_location)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getSpecialization());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getEmail());
            pstmt.setString(6, doctor.getLicenseNumber());
            pstmt.setString(7, doctor.getDescription());
            pstmt.setDate(8, Date.valueOf(doctor.getHireDate()));
            pstmt.setString(9, doctor.getWorkingHours());
            pstmt.setString(10, doctor.getOfficeLocation());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setEmail(rs.getString("email"));
                doctor.setLicenseNumber(rs.getString("license_number"));
                doctor.setDescription(rs.getString("description"));
                doctor.setHireDate(rs.getDate("hire_date").toLocalDate());
                doctor.setWorkingHours(rs.getString("working_hours"));
                doctor.setOfficeLocation(rs.getString("office_location"));
                
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctors: " + e.getMessage());
        }
        
        return doctors;
    }

    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setEmail(rs.getString("email"));
                doctor.setLicenseNumber(rs.getString("license_number"));
                doctor.setDescription(rs.getString("description"));
                doctor.setHireDate(rs.getDate("hire_date").toLocalDate());
                doctor.setWorkingHours(rs.getString("working_hours"));
                doctor.setOfficeLocation(rs.getString("office_location"));
                
                return doctor;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor: " + e.getMessage());
        }
        
        return null;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = """
            UPDATE doctors SET first_name = ?, last_name = ?, specialization = ?, 
                              phone = ?, email = ?, license_number = ?, description = ?, 
                              working_hours = ?, office_location = ? WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getSpecialization());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getEmail());
            pstmt.setString(6, doctor.getLicenseNumber());
            pstmt.setString(7, doctor.getDescription());
            pstmt.setString(8, doctor.getWorkingHours());
            pstmt.setString(9, doctor.getOfficeLocation());
            pstmt.setInt(10, doctor.getId());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }
}
