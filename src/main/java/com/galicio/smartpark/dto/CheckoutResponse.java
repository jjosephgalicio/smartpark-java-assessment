package com.galicio.smartpark.dto;

import java.time.LocalDateTime;

public class CheckoutResponse {
    private String licensePlate;
    private String lotId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private long totalMinutesParked;
    private double totalCost;

    public CheckoutResponse(String licensePlate, String lotId, LocalDateTime checkInTime, LocalDateTime checkOutTime, long totalMinutesParked, double totalCost) {
        this.licensePlate = licensePlate;
        this.lotId = lotId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.totalMinutesParked = totalMinutesParked;
        this.totalCost = totalCost;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getLotId() { return lotId; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public long getTotalMinutesParked() { return totalMinutesParked; }
    public double getTotalCost() { return totalCost; }
}