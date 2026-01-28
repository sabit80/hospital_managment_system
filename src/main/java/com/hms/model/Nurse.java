package com.hms.model;

import java.time.LocalDate;

public class Nurse {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int licenseNumber;
    private String description;
    private LocalDate hireDate;
    private String workingHours;
    private String floor;
    private String roomNumber;
    private String department;

    public Nurse() {
        this.hireDate = LocalDate.now();
    }

    public Nurse(int id, String firstName, String lastName, String phone,
                 String email, int licenseNumber, String description,
                 String floor, String roomNumber, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.description = description;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.department = department;
        this.hireDate = LocalDate.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(int licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getLocation() {
        return "Floor " + floor + ", Room " + roomNumber;
    }
}
