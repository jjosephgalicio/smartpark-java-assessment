package com.galicio.smartpark.service;

import com.galicio.smartpark.model.ParkingLot;
import com.galicio.smartpark.model.ParkingSession;
import com.galicio.smartpark.repository.ParkingLotRepository;
import com.galicio.smartpark.repository.ParkingSessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutoEvictionScheduler {

    private final ParkingSessionRepository sessionRepository;
    private final ParkingLotRepository lotRepository;

    public AutoEvictionScheduler(ParkingSessionRepository sessionRepository, ParkingLotRepository lotRepository) {
        this.sessionRepository = sessionRepository;
        this.lotRepository = lotRepository;
    }

    // Runs every 30 seconds to enforce the 15-minute limit rule
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void removeExpiredParkingSessions() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);
        List<ParkingSession> expiredSessions = sessionRepository.findByActiveTrueAndCheckInTimeBefore(cutoff);

        for (ParkingSession session : expiredSessions) {
            LocalDateTime now = LocalDateTime.now();
            long minutes = Duration.between(session.getCheckInTime(), now).toMinutes();

            session.setActive(false);
            session.setCheckOutTime(now);
            session.setTotalCost(minutes * session.getParkingLot().getCostPerMinute());
            sessionRepository.save(session);

            ParkingLot lot = session.getParkingLot();
            lot.setOccupiedSpaces(Math.max(0, lot.getOccupiedSpaces() - 1));
            lotRepository.save(lot);

            System.out.println("AUTO-REMOVED Vehicle: " + session.getVehicle().getLicensePlate() + " from Lot: " + lot.getLotId());
        }
    }
}