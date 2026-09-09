package com.galicio.smartpark.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "license_plate", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private ParkingLot parkingLot;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private boolean active = true;
    private Double totalCost;

    public ParkingSession() {}

    public ParkingSession(Vehicle vehicle, ParkingLot parkingLot, LocalDateTime checkInTime) {
        this.vehicle = vehicle;
        this.parkingLot = parkingLot;
        this.checkInTime = checkInTime;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public ParkingLot getParkingLot() { return parkingLot; }
    public void setParkingLot(ParkingLot parkingLot) { this.parkingLot = parkingLot; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
}