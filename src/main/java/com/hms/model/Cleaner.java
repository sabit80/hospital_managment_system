package com.hms.model;

import java.time.LocalDate;

public class Cleaner {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String description;
    private LocalDate hireDate;
    private String workingHours;
    private String assignedFloor;
    private String assignedArea;
    private String shift;
    private String status;

    public Cleaner() {
        this.hireDate = LocalDate.now();
        this.status = "Available";
    }

    public Cleaner(int id, String firstName, String lastName, String phone,
                   String email, String description, String assignedFloor,
                   String assignedArea, String shift, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.assignedFloor = assignedFloor;
        this.assignedArea = assignedArea;
        this.shift = shift;
        this.status = status == null || status.isBlank() ? "Available" : status;
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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public String getAssignedFloor() { return assignedFloor; }
    public void setAssignedFloor(String assignedFloor) { this.assignedFloor = assignedFloor; }

    public String getAssignedArea() { return assignedArea; }
    public void setAssignedArea(String assignedArea) { this.assignedArea = assignedArea; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAssignment() {
        return "Floor " + assignedFloor + ", " + assignedArea + " (" + shift + " shift)";
    }
}
