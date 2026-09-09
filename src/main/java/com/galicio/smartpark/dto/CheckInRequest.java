package com.galicio.smartpark.dto;

public class CheckInRequest {
    private String licensePlate;
    private String lotId;

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getLotId() { return lotId; }
    public void setLotId(String lotId) { this.lotId = lotId; }
}