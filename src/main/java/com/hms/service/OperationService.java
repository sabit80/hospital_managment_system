package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Operation;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperationService {
    private final Connection connection;

    public OperationService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public List<Operation> getOperationsByDoctorAndDate(int doctorId, LocalDate date) {
        List<Operation> operations = new ArrayList<>();
        String sql = """
            SELECT o.id, o.doctor_id, o.patient_id, o.operation_date, o.operation_time,
                   o.description, o.status, p.first_name || ' ' || p.last_name AS patient_name
            FROM operations o
            LEFT JOIN patients p ON o.patient_id = p.id
            WHERE o.doctor_id = ? AND o.operation_date = ?
            ORDER BY o.operation_time ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDoctorId(rs.getInt("doctor_id"));
                operation.setPatientId(rs.getInt("patient_id"));
                operation.setOperationDate(rs.getDate("operation_date").toLocalDate());
                operation.setOperationTime(rs.getString("operation_time"));
                operation.setDescription(rs.getString("description"));
                operation.setStatus(rs.getString("status"));
                operation.setPatientName(rs.getString("patient_name"));
                operations.add(operation);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching operations: " + e.getMessage());
        }

        return operations;
    }
}
