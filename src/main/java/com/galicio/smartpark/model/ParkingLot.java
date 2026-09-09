package com.galicio.smartpark.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Entity
public class ParkingLot {

    @Id
    @Size(max = 50, message = "Lot ID must not exceed 50 characters")
    private String lotId;

    @NotNull(message = "Location is required")
    private String location;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    private int occupiedSpaces = 0;

    @Min(value = 0, message = "Cost per minute cannot be negative")
    private double costPerMinute;

    public ParkingLot() {}

    public ParkingLot(String lotId, String location, int capacity, double costPerMinute) {
        this.lotId = lotId;
        this.location = location;
        this.capacity = capacity;
        this.costPerMinute = costPerMinute;
        this.occupiedSpaces = 0;
    }

    // Getters and Setters
    public String getLotId() { return lotId; }
    public void setLotId(String lotId) { this.lotId = lotId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getOccupiedSpaces() { return occupiedSpaces; }
    public void setOccupiedSpaces(int occupiedSpaces) { this.occupiedSpaces = occupiedSpaces; }

    public double getCostPerMinute() { return costPerMinute; }
    public void setCostPerMinute(double costPerMinute) { this.costPerMinute = costPerMinute; }
}