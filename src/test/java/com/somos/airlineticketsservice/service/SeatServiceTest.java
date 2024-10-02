package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.dto.SeatHold;
import com.somos.airlineticketsservice.entity.Customer;
import com.somos.airlineticketsservice.entity.PriceTier;
import com.somos.airlineticketsservice.entity.Seat;
import com.somos.airlineticketsservice.entity.SeatStatus;
import com.somos.airlineticketsservice.repository.CustomerRepository;
import com.somos.airlineticketsservice.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testReleaseExpiredHolds() {
        // Arrange
        Seat seat = new Seat();
        seat.setStatus(SeatStatus.HELD);
        seat.setHoldTime(LocalDateTime.now().minusMinutes(3)); // Expired hold

        List<Seat> heldSeats = List.of(seat);

        when(seatRepository.findHeldSeats()).thenReturn(heldSeats);

        // Act
        seatService.releaseExpiredHolds();

        // Assert
        assertEquals(SeatStatus.AVAILABLE, seat.getStatus());
        verify(seatRepository, times(1)).saveAll(anyList());
    }
}