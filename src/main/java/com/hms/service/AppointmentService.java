package com.hms.service;

import com.hms.database.DatabaseManager;
import com.hms.model.Appointment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private final Connection connection;

    public AppointmentService() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public boolean addAppointment(Appointment appointment) {
        String sql = """
            INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            pstmt.setString(4, appointment.getAppointmentTime());
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getNotes());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes,
                   p.first_name || ' ' || p.last_name AS patient_name,
                   d.first_name || ' ' || d.last_name AS doctor_name
            FROM appointments a
            LEFT JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            ORDER BY a.appointment_date DESC, a.appointment_time ASC
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPatientId(rs.getInt("patient_id"));
                    appointment.setDoctorId(rs.getInt("doctor_id"));
                    String dateValue = rs.getString("appointment_date");
                    if (dateValue != null && !dateValue.isBlank()) {
                        LocalDate parsedDate = parseDateSafely(dateValue);
                        appointment.setAppointmentDate(parsedDate);
                    }
                    appointment.setAppointmentTime(rs.getString("appointment_time"));
                    appointment.setStatus(rs.getString("status"));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPatientName(rs.getString("patient_name"));
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointments.add(appointment);
                } catch (Exception e) {
                    System.err.println("Skipping malformed appointment row: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes,
                   p.first_name || ' ' || p.last_name AS patient_name,
                   d.first_name || ' ' || d.last_name AS doctor_name
            FROM appointments a
            LEFT JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            WHERE a.appointment_date = ?
            ORDER BY a.appointment_time ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPatientId(rs.getInt("patient_id"));
                    appointment.setDoctorId(rs.getInt("doctor_id"));
                    String dateValue = rs.getString("appointment_date");
                    if (dateValue != null && !dateValue.isBlank()) {
                        LocalDate parsedDate = parseDateSafely(dateValue);
                        appointment.setAppointmentDate(parsedDate);
                    }
                    appointment.setAppointmentTime(rs.getString("appointment_time"));
                    appointment.setStatus(rs.getString("status"));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPatientName(rs.getString("patient_name"));
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointments.add(appointment);
                } catch (Exception e) {
                    System.err.println("Skipping malformed appointment row: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments by date: " + e.getMessage());
        }

        return appointments;
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes,
                   p.first_name || ' ' || p.last_name AS patient_name,
                   d.first_name || ' ' || d.last_name AS doctor_name
            FROM appointments a
            LEFT JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            WHERE a.appointment_date BETWEEN ? AND ?
            ORDER BY a.appointment_date ASC, a.appointment_time ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPatientId(rs.getInt("patient_id"));
                    appointment.setDoctorId(rs.getInt("doctor_id"));
                    String dateValue = rs.getString("appointment_date");
                    if (dateValue != null && !dateValue.isBlank()) {
                        LocalDate parsedDate = parseDateSafely(dateValue);
                        appointment.setAppointmentDate(parsedDate);
                    }
                    appointment.setAppointmentTime(rs.getString("appointment_time"));
                    appointment.setStatus(rs.getString("status"));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPatientName(rs.getString("patient_name"));
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointments.add(appointment);
                } catch (Exception e) {
                    System.err.println("Skipping malformed appointment row: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments by range: " + e.getMessage());
        }

        return appointments;
    }

    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    public List<Appointment> getAppointmentsByDoctorAndDate(int doctorId, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
            SELECT a.id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes,
                   p.first_name || ' ' || p.last_name AS patient_name,
                   d.first_name || ' ' || d.last_name AS doctor_name
            FROM appointments a
            LEFT JOIN patients p ON a.patient_id = p.id
            LEFT JOIN doctors d ON a.doctor_id = d.id
            WHERE a.doctor_id = ? AND a.appointment_date = ?
            ORDER BY a.appointment_time ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    Appointment appointment = new Appointment();
                    appointment.setId(rs.getInt("id"));
                    appointment.setPatientId(rs.getInt("patient_id"));
                    appointment.setDoctorId(rs.getInt("doctor_id"));
                    String dateValue = rs.getString("appointment_date");
                    if (dateValue != null && !dateValue.isBlank()) {
                        LocalDate parsedDate = parseDateSafely(dateValue);
                        appointment.setAppointmentDate(parsedDate);
                    }
                    appointment.setAppointmentTime(rs.getString("appointment_time"));
                    appointment.setStatus(rs.getString("status"));
                    appointment.setNotes(rs.getString("notes"));
                    appointment.setPatientName(rs.getString("patient_name"));
                    appointment.setDoctorName(rs.getString("doctor_name"));
                    appointments.add(appointment);
                } catch (Exception e) {
                    System.err.println("Skipping malformed appointment row: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments by doctor/date: " + e.getMessage());
        }

        return appointments;
    }

    private LocalDate parseDateSafely(String raw) {
        try {
            return LocalDate.parse(raw);
        } catch (Exception ignored) {
            // Try stripping time part if present (e.g., "2026-01-30 10:00:00" or "2026-01-30T10:00")
            String trimmed = raw;
            int spaceIndex = trimmed.indexOf(' ');
            if (spaceIndex > 0) {
                trimmed = trimmed.substring(0, spaceIndex);
            }
            int tIndex = trimmed.indexOf('T');
            if (tIndex > 0) {
                trimmed = trimmed.substring(0, tIndex);
            }
            try {
                return LocalDate.parse(trimmed);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
