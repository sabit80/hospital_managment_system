package com.hms.service;

import com.hms.database.DatabaseManager;
import java.sql.*;

public class WardService {
    private final Connection connection;

    public WardService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void ensureDefaultWardsAndBeds() {
        String countSql = "SELECT COUNT(*) AS total FROM wards";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next() && rs.getInt("total") > 0) {
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error checking wards: " + e.getMessage());
            return;
        }

        try {
            connection.setAutoCommit(false);
            int wardId = insertWard("General Ward A", "Ward", 10);
            insertBeds(wardId, 10);
            int icuId = insertWard("ICU", "ICU", 6);
            insertBeds(icuId, 6);
            int ccuId = insertWard("CCU", "CCU", 4);
            insertBeds(ccuId, 4);
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Error seeding wards/beds: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackError) {
                System.err.println("Rollback failed: " + rollbackError.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting autocommit: " + e.getMessage());
            }
        }
    }

    private int insertWard(String name, String type, int totalBeds) throws SQLException {
        String sql = "INSERT INTO wards (name, type, total_beds) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, totalBeds);
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        throw new SQLException("Failed to insert ward: " + name);
    }

    private void insertBeds(int wardId, int totalBeds) throws SQLException {
        String sql = "INSERT INTO beds (ward_id, bed_number, status) VALUES (?, ?, 'Vacant')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 1; i <= totalBeds; i++) {
                pstmt.setInt(1, wardId);
                pstmt.setString(2, String.valueOf(i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
}
