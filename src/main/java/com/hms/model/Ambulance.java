package com.hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ambulance {
    private int id;
    private String vehicleNumber;
    private String type;
    private String driverName;
    private String driverPhone;
    private String status;
    private String currentLocation;
    private LocalDateTime lastServiceDate;
    private int capacity;
    private boolean equipped;
    private String equipmentList;
    private String fuelStatus;
    private double operationalCost;
    private double serviceFee;

    public Ambulance() {
        this.lastServiceDate = LocalDateTime.now();
    }

    public Ambulance(int id, String vehicleNumber, String type, String driverName,
                     String driverPhone, String status, String currentLocation) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.status = status;
        this.currentLocation = currentLocation;
        this.lastServiceDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public LocalDateTime getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDateTime lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }

    public String getEquipmentList() { return equipmentList; }
    public void setEquipmentList(String equipmentList) { this.equipmentList = equipmentList; }

    public String getFuelStatus() { return fuelStatus; }
    public void setFuelStatus(String fuelStatus) { this.fuelStatus = fuelStatus; }

    public double getOperationalCost() { return operationalCost; }
    public void setOperationalCost(double operationalCost) { this.operationalCost = operationalCost; }

    public double getServiceFee() { return serviceFee; }
    public void setServiceFee(double serviceFee) { this.serviceFee = serviceFee; }
}
