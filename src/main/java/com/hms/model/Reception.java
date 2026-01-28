package com.hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reception {
    private int id;
    private String visitorName;
    private String visitorPhone;
    private String patientName;
    private int patientId;
    private String purposeOfVisit;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String receptionistName;
    private String remarks;
    private String status;

    public Reception() {
        this.checkInTime = LocalDateTime.now();
    }

    public Reception(int id, String visitorName, String visitorPhone, String patientName,
                     int patientId, String purposeOfVisit, String receptionistName) {
        this.id = id;
        this.visitorName = visitorName;
        this.visitorPhone = visitorPhone;
        this.patientName = patientName;
        this.patientId = patientId;
        this.purposeOfVisit = purposeOfVisit;
        this.receptionistName = receptionistName;
        this.checkInTime = LocalDateTime.now();
        this.status = "Checked In";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }

    public String getVisitorPhone() { return visitorPhone; }
    public void setVisitorPhone(String visitorPhone) { this.visitorPhone = visitorPhone; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPurposeOfVisit() { return purposeOfVisit; }
    public void setPurposeOfVisit(String purposeOfVisit) { this.purposeOfVisit = purposeOfVisit; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getReceptionistName() { return receptionistName; }
    public void setReceptionistName(String receptionistName) { this.receptionistName = receptionistName; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
