package com.hms.model;

import java.time.LocalDate;

public class Doctor {
    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private String licenseNumber;
    private String description;
    private LocalDate hireDate;
    private String workingHours;
    private String officeLocation;

    public Doctor() {
        this.hireDate = LocalDate.now();
    }

    public Doctor(int id, String firstName, String lastName, String specialization,
                  String phone, String email, String licenseNumber, String description) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.description = description;
        this.hireDate = LocalDate.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
