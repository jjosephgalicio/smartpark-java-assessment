package com.galicio.smartpark.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

@Entity
public class Vehicle {

    @Id
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "License plate must contain only letters, numbers, and dashes")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Vehicle type is required")
    private VehicleType type;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Owner name must contain only letters and spaces")
    @NotNull(message = "Owner name is required")
    private String ownerName;

    public Vehicle() {}

    public Vehicle(String licensePlate, VehicleType type, String ownerName) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.ownerName = ownerName;
    }

    // Getters and Setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}