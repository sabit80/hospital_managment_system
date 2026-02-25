package com.hms.model;

public class Ward {
    private int id;
    private String name;
    private String type;
    private int totalBeds;

    public Ward() {
    }

    public Ward(int id, String name, String type, int totalBeds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.totalBeds = totalBeds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(int totalBeds) {
        this.totalBeds = totalBeds;
    }
}
