package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Ambulance;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AmbulanceService {
    private Connection connection;

    public AmbulanceService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addAmbulance(Ambulance ambulance) {
        String sql = """
            INSERT INTO ambulances (vehicle_number, type, driver_name, driver_phone, status,
                                   current_location, last_service_date, capacity, equipped, equipment_list, fuel_status,
                                   operational_cost, service_fee)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ambulance.getVehicleNumber());
            pstmt.setString(2, ambulance.getType());
            pstmt.setString(3, ambulance.getDriverName());
            pstmt.setString(4, ambulance.getDriverPhone());
            pstmt.setString(5, ambulance.getStatus());
            pstmt.setString(6, ambulance.getCurrentLocation());
            pstmt.setString(7, ambulance.getLastServiceDate().toString());
            pstmt.setInt(8, ambulance.getCapacity());
            pstmt.setBoolean(9, ambulance.isEquipped());
            pstmt.setString(10, ambulance.getEquipmentList());
            pstmt.setString(11, ambulance.getFuelStatus());
            pstmt.setDouble(12, ambulance.getOperationalCost());
            pstmt.setDouble(13, ambulance.getServiceFee());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding ambulance: " + e.getMessage());
            return false;
        }
    }

    public List<Ambulance> getAllAmbulances() {
        List<Ambulance> ambulances = new ArrayList<>();
        String sql = "SELECT * FROM ambulances ORDER BY id DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Ambulance ambulance = new Ambulance();
                ambulance.setId(rs.getInt("id"));
                ambulance.setVehicleNumber(rs.getString("vehicle_number"));
                ambulance.setType(rs.getString("type"));
                ambulance.setDriverName(rs.getString("driver_name"));
                ambulance.setDriverPhone(rs.getString("driver_phone"));
                ambulance.setStatus(rs.getString("status"));
                ambulance.setCurrentLocation(rs.getString("current_location"));
                ambulance.setLastServiceDate(LocalDateTime.parse(rs.getString("last_service_date")));
                ambulance.setCapacity(rs.getInt("capacity"));
                ambulance.setEquipped(rs.getBoolean("equipped"));
                ambulance.setEquipmentList(rs.getString("equipment_list"));
                ambulance.setFuelStatus(rs.getString("fuel_status"));
                ambulance.setOperationalCost(rs.getDouble("operational_cost"));
                ambulance.setServiceFee(rs.getDouble("service_fee"));
                
                ambulances.add(ambulance);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ambulances: " + e.getMessage());
        }
        
        return ambulances;
    }

    public boolean updateAmbulance(Ambulance ambulance) {
        String sql = """
            UPDATE ambulances SET vehicle_number = ?, type = ?, driver_name = ?, driver_phone = ?,
                                 status = ?, current_location = ?, capacity = ?, equipped = ?,
                                 equipment_list = ?, fuel_status = ?, operational_cost = ?, service_fee = ? WHERE id = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ambulance.getVehicleNumber());
            pstmt.setString(2, ambulance.getType());
            pstmt.setString(3, ambulance.getDriverName());
            pstmt.setString(4, ambulance.getDriverPhone());
            pstmt.setString(5, ambulance.getStatus());
            pstmt.setString(6, ambulance.getCurrentLocation());
            pstmt.setInt(7, ambulance.getCapacity());
            pstmt.setBoolean(8, ambulance.isEquipped());
            pstmt.setString(9, ambulance.getEquipmentList());
            pstmt.setString(10, ambulance.getFuelStatus());
            pstmt.setDouble(11, ambulance.getOperationalCost());
            pstmt.setDouble(12, ambulance.getServiceFee());
            pstmt.setInt(13, ambulance.getId());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating ambulance: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAmbulance(int id) {
        String sql = "DELETE FROM ambulances WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting ambulance: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(int id, String status, String location) {
        String sql = "UPDATE ambulances SET status = ?, current_location = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, location);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating ambulance status: " + e.getMessage());
            return false;
        }
    }
}
