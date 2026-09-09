package com.galicio.smartpark.service;

import com.galicio.smartpark.dto.CheckoutResponse;
import com.galicio.smartpark.dto.OccupancyResponse;
import com.galicio.smartpark.model.ParkingLot;
import com.galicio.smartpark.model.ParkingSession;
import com.galicio.smartpark.model.Vehicle;
import com.galicio.smartpark.repository.ParkingLotRepository;
import com.galicio.smartpark.repository.ParkingSessionRepository;
import com.galicio.smartpark.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ParkingLotRepository lotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSessionRepository sessionRepository;

    public ParkingService(ParkingLotRepository lotRepository,
                          VehicleRepository vehicleRepository,
                          ParkingSessionRepository sessionRepository) {
        this.lotRepository = lotRepository;
        this.vehicleRepository = vehicleRepository;
        this.sessionRepository = sessionRepository;
    }

    public ParkingLot registerLot(ParkingLot lot) {
        return lotRepository.save(lot);
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public ParkingSession checkIn(String licensePlate, String lotId) {
        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + licensePlate));

        ParkingLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found: " + lotId));

        // Rule: Ensure vehicle is not parked elsewhere
        sessionRepository.findByVehicleAndActiveTrue(vehicle).ifPresent(s -> {
            throw new IllegalStateException("Vehicle is already checked into lot: " + s.getParkingLot().getLotId());
        });

        // Rule: Prevent check-in if full
        if (lot.getOccupiedSpaces() >= lot.getCapacity()) {
            throw new IllegalStateException("Parking lot is full");
        }

        lot.setOccupiedSpaces(lot.getOccupiedSpaces() + 1);
        lotRepository.save(lot);

        ParkingSession session = new ParkingSession(vehicle, lot, LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Transactional
    public CheckoutResponse checkOut(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findById(licensePlate)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + licensePlate));

        ParkingSession session = sessionRepository.findByVehicleAndActiveTrue(vehicle)
                .orElseThrow(() -> new IllegalArgumentException("No active parking session found for vehicle: " + licensePlate));

        LocalDateTime now = LocalDateTime.now();
        long minutesParked = Math.max(1, Duration.between(session.getCheckInTime(), now).toMinutes());
        double totalCost = minutesParked * session.getParkingLot().getCostPerMinute();

        session.setCheckOutTime(now);
        session.setActive(false);
        session.setTotalCost(totalCost);
        sessionRepository.save(session);

        ParkingLot lot = session.getParkingLot();
        lot.setOccupiedSpaces(Math.max(0, lot.getOccupiedSpaces() - 1));
        lotRepository.save(lot);

        return new CheckoutResponse(licensePlate, lot.getLotId(), session.getCheckInTime(), now, minutesParked, totalCost);
    }

    public OccupancyResponse getOccupancy(String lotId) {
        ParkingLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found: " + lotId));
        return new OccupancyResponse(lot.getLotId(), lot.getLocation(), lot.getCapacity(), lot.getOccupiedSpaces());
    }

    public List<Vehicle> getParkedVehicles(String lotId) {
        ParkingLot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking lot not found: " + lotId));

        return sessionRepository.findByParkingLotAndActiveTrue(lot).stream()
                .map(ParkingSession::getVehicle)
                .collect(Collectors.toList());
    }
}