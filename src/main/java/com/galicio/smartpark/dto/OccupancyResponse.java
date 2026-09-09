package com.galicio.smartpark.dto;

public class OccupancyResponse {
    private String lotId;
    private String location;
    private int capacity;
    private int occupiedSpaces;
    private int availableSpaces;

    public OccupancyResponse(String lotId, String location, int capacity, int occupiedSpaces) {
        this.lotId = lotId;
        this.location = location;
        this.capacity = capacity;
        this.occupiedSpaces = occupiedSpaces;
        this.availableSpaces = capacity - occupiedSpaces;
    }

    public String getLotId() { return lotId; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public int getOccupiedSpaces() { return occupiedSpaces; }
    public int getAvailableSpaces() { return availableSpaces; }
}