package com.galicio.smartpark.controller;

import com.galicio.smartpark.dto.CheckInRequest;
import com.galicio.smartpark.model.ParkingLot;
import com.galicio.smartpark.model.Vehicle;
import com.galicio.smartpark.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/lots")
    public ResponseEntity<?> registerLot(@Valid @RequestBody ParkingLot lot) {
        return ResponseEntity.ok(parkingService.registerLot(lot));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(parkingService.registerVehicle(vehicle));
    }

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        try {
            return ResponseEntity.ok(parkingService.checkIn(request.getLicensePlate(), request.getLotId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/check-out/{licensePlate}")
    public ResponseEntity<?> checkOut(@PathVariable String licensePlate) {
        try {
            return ResponseEntity.ok(parkingService.checkOut(licensePlate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lots/{lotId}/occupancy")
    public ResponseEntity<?> getOccupancy(@PathVariable String lotId) {
        try {
            return ResponseEntity.ok(parkingService.getOccupancy(lotId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lots/{lotId}/vehicles")
    public ResponseEntity<?> getParkedVehicles(@PathVariable String lotId) {
        try {
            return ResponseEntity.ok(parkingService.getParkedVehicles(lotId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}