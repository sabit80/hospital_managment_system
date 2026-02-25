package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Bed;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BedService {
    private final Connection connection;

    public BedService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public int getVacantCountByType(String wardType) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM beds b
            JOIN wards w ON b.ward_id = w.id
            WHERE b.status = 'Vacant' AND w.type = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, wardType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting vacant beds: " + e.getMessage());
        }

        return 0;
    }

    public List<Bed> getAvailableBeds() {
        List<Bed> beds = new ArrayList<>();
        String sql = """
            SELECT b.id, b.ward_id, b.bed_number, b.status, b.patient_id,
                   w.name AS ward_name, w.type AS ward_type
            FROM beds b
            JOIN wards w ON b.ward_id = w.id
            WHERE b.status = 'Vacant'
            ORDER BY w.type, w.name, b.bed_number
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bed bed = new Bed();
                bed.setId(rs.getInt("id"));
                bed.setWardId(rs.getInt("ward_id"));
                bed.setBedNumber(rs.getString("bed_number"));
                bed.setStatus(rs.getString("status"));
                bed.setPatientId((Integer) rs.getObject("patient_id"));
                bed.setWardName(rs.getString("ward_name"));
                bed.setWardType(rs.getString("ward_type"));
                beds.add(bed);
            }
        } catch (SQLException e) {
            System.err.println("Error loading beds: " + e.getMessage());
        }

        return beds;
    }

    public boolean assignBedToPatient(int bedId, int patientId) {
        String sql = "UPDATE beds SET status = 'Occupied', patient_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, bedId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning bed: " + e.getMessage());
            return false;
        }
    }
}
