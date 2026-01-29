package com.hms.database;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hospital.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            initializeTables();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initializeTables() {
        createPatientsTable();
        createDoctorsTable();
        createAppointmentsTable();
        createNursesTable();
        createCleanersTable();
        createReceptionTable();
        createAmbulancesTable();
        createFinanceTable();
        runMigrations();
    }

    private void createPatientsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS patients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                gender TEXT,
                date_of_birth DATE,
                phone TEXT,
                email TEXT,
                address TEXT,
                blood_group TEXT,
                registration_date DATE DEFAULT CURRENT_DATE
            )
        """;
        executeUpdate(sql);
    }

    private void createDoctorsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS doctors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                specialization TEXT,
                phone TEXT,
                email TEXT,
                license_number TEXT UNIQUE,
                description TEXT,
                hire_date DATE,
                working_hours TEXT,
                office_location TEXT
            )
        """;
        executeUpdate(sql);
    }

    private void createAppointmentsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER,
                doctor_id INTEGER,
                appointment_date DATE,
                appointment_time TEXT,
                status TEXT DEFAULT 'Scheduled',
                notes TEXT,
                FOREIGN KEY (patient_id) REFERENCES patients(id),
                FOREIGN KEY (doctor_id) REFERENCES doctors(id)
            )
        """;
        executeUpdate(sql);
    }

    private void createNursesTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS nurses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                phone TEXT,
                email TEXT,
                license_number INTEGER,
                description TEXT,
                hire_date DATE,
                working_hours TEXT,
                floor TEXT,
                room_number TEXT,
                department TEXT
            )
        """;
        executeUpdate(sql);
    }

    private void createCleanersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cleaners (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                phone TEXT,
                email TEXT,
                description TEXT,
                hire_date DATE,
                working_hours TEXT,
                assigned_floor TEXT,
                assigned_area TEXT,
                shift TEXT
            )
        """;
        executeUpdate(sql);
    }

    private void createReceptionTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS reception (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                visitor_name TEXT NOT NULL,
                visitor_phone TEXT,
                patient_name TEXT,
                patient_id INTEGER,
                purpose_of_visit TEXT,
                check_in_time TEXT,
                check_out_time TEXT,
                receptionist_name TEXT,
                remarks TEXT,
                status TEXT DEFAULT 'Checked In'
            )
        """;
        executeUpdate(sql);
    }

    private void createAmbulancesTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS ambulances (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                vehicle_number TEXT UNIQUE NOT NULL,
                type TEXT,
                driver_name TEXT,
                driver_phone TEXT,
                status TEXT DEFAULT 'Available',
                current_location TEXT,
                last_service_date TEXT,
                capacity INTEGER,
                equipped BOOLEAN DEFAULT 0,
                equipment_list TEXT,
                fuel_status TEXT
            )
        """;
        executeUpdate(sql);
    }

    private void createFinanceTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS finance (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                transaction_date DATE NOT NULL,
                type TEXT NOT NULL,
                category TEXT,
                description TEXT,
                amount REAL NOT NULL,
                payment_method TEXT,
                remarks TEXT
            )
        """;
        executeUpdate(sql);
    }

    private void executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error executing SQL: " + e.getMessage());
        }
    }

    private void runMigrations() {
        // Add missing columns if they don't exist
        addColumnIfNotExists("doctors", "description", "TEXT");
        addColumnIfNotExists("doctors", "working_hours", "TEXT");
        addColumnIfNotExists("doctors", "office_location", "TEXT");
        addColumnIfNotExists("doctors", "hire_date", "DATE");

        addColumnIfNotExists("appointments", "patient_id", "INTEGER");
        addColumnIfNotExists("appointments", "doctor_id", "INTEGER");
        addColumnIfNotExists("appointments", "appointment_date", "DATE");
        addColumnIfNotExists("appointments", "appointment_time", "TEXT");
        addColumnIfNotExists("appointments", "status", "TEXT");
        addColumnIfNotExists("appointments", "notes", "TEXT");
    }

    private void addColumnIfNotExists(String tableName, String columnName, String columnType) {
        try {
            // Check if column exists
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, columnName);
            
            if (!columns.next()) {
                // Column doesn't exist, add it
                String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
                executeUpdate(sql);
                System.out.println("Added column " + columnName + " to table " + tableName);
            }
            columns.close();
        } catch (SQLException e) {
            System.err.println("Error checking/adding column: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
