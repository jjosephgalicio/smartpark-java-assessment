package com.galicio.smartpark.service;

import com.galicio.smartpark.dto.CheckoutResponse;
import com.galicio.smartpark.model.ParkingLot;
import com.galicio.smartpark.model.ParkingSession;
import com.galicio.smartpark.model.Vehicle;
import com.galicio.smartpark.model.VehicleType;
import com.galicio.smartpark.repository.ParkingLotRepository;
import com.galicio.smartpark.repository.ParkingSessionRepository;
import com.galicio.smartpark.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingLotRepository lotRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingSessionRepository sessionRepository;

    @InjectMocks
    private ParkingService parkingService;

    private ParkingLot testLot;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testLot = new ParkingLot("LOT-001", "Downtown", 5, 1.50);
        testVehicle = new Vehicle("ABC-1234", VehicleType.CAR, "John Doe");
    }

    // FUNCTIONAL TEST SCENARIOS

    @Test
    void testCheckIn_Success() {
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(testVehicle));
        when(lotRepository.findById("LOT-001")).thenReturn(Optional.of(testLot));
        when(sessionRepository.findByVehicleAndActiveTrue(testVehicle)).thenReturn(Optional.empty());
        when(sessionRepository.save(any(ParkingSession.class))).thenAnswer(i -> i.getArguments()[0]);

        ParkingSession session = parkingService.checkIn("ABC-1234", "LOT-001");

        assertNotNull(session);
        assertEquals(1, testLot.getOccupiedSpaces());
        verify(lotRepository, times(1)).save(testLot);
    }

    @Test
    void testCheckIn_LotFull_ThrowsException() {
        testLot.setOccupiedSpaces(5); // Lot at capacity
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(testVehicle));
        when(lotRepository.findById("LOT-001")).thenReturn(Optional.of(testLot));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> parkingService.checkIn("ABC-1234", "LOT-001")
        );

        assertEquals("Parking lot is full", exception.getMessage());
    }

    @Test
    void testCheckOut_Success() {
        testLot.setOccupiedSpaces(1);
        ParkingSession session = new ParkingSession(testVehicle, testLot, LocalDateTime.now().minusMinutes(10));

        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(testVehicle));
        when(sessionRepository.findByVehicleAndActiveTrue(testVehicle)).thenReturn(Optional.of(session));

        CheckoutResponse response = parkingService.checkOut("ABC-1234");

        assertNotNull(response);
        assertEquals("ABC-1234", response.getLicensePlate());
        assertEquals(0, testLot.getOccupiedSpaces());
        assertFalse(session.isActive());
    }


    // NEGATIVE TESTS - EDGE CASE SCENARIOS

    @Test
    void testCheckIn_VehicleAlreadyParked_ThrowsException() {
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(testVehicle));
        when(lotRepository.findById("LOT-001")).thenReturn(Optional.of(testLot));

        ParkingSession activeSession = new ParkingSession(testVehicle, testLot, LocalDateTime.now());
        when(sessionRepository.findByVehicleAndActiveTrue(testVehicle))
                .thenReturn(Optional.of(activeSession));

        // Accepts RuntimeException or IllegalStateException with any message
        assertThrows(
                RuntimeException.class,
                () -> parkingService.checkIn("ABC-1234", "LOT-001")
        );
    }

    @Test
    void testCheckOut_VehicleNotFound_ThrowsException() {
        when(vehicleRepository.findById("NON-EXISTENT")).thenReturn(Optional.empty());

        // Accepts RuntimeException or IllegalArgumentException with any message
        assertThrows(
                RuntimeException.class,
                () -> parkingService.checkOut("NON-EXISTENT")
        );
    }

    @Test
    void testCheckOut_NoActiveSession_ThrowsException() {
        when(vehicleRepository.findById("ABC-1234")).thenReturn(Optional.of(testVehicle));
        when(sessionRepository.findByVehicleAndActiveTrue(testVehicle)).thenReturn(Optional.empty());

        // Accepts RuntimeException or IllegalStateException with any message
        assertThrows(
                RuntimeException.class,
                () -> parkingService.checkOut("ABC-1234")
        );
    }
}