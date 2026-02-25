package com.hms.model;

public class Bed {
    private int id;
    private int wardId;
    private String wardName;
    private String wardType;
    private String bedNumber;
    private String status;
    private Integer patientId;

    public Bed() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardType() {
        return wardType;
    }

    public void setWardType(String wardType) {
        this.wardType = wardType;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getDisplayName() {
        String ward = wardName == null ? "Ward" : wardName;
        String type = wardType == null ? "" : wardType;
        String number = bedNumber == null ? "" : bedNumber;
        return ward + " - Bed " + number + (type.isBlank() ? "" : " (" + type + ")");
    }
}
