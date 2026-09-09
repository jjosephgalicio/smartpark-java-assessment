package com.galicio.smartpark.repository;

import com.galicio.smartpark.model.ParkingSession;
import com.galicio.smartpark.model.ParkingLot;
import com.galicio.smartpark.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    // Check if vehicle has an active parking session anywhere
    Optional<ParkingSession> findByVehicleAndActiveTrue(Vehicle vehicle);

    // Get all active sessions in a specific lot
    List<ParkingSession> findByParkingLotAndActiveTrue(ParkingLot parkingLot);

    // Find active sessions checked in before a specific cutoff time (for 15-min auto removal)
    List<ParkingSession> findByActiveTrueAndCheckInTimeBefore(LocalDateTime cutoffTime);
}