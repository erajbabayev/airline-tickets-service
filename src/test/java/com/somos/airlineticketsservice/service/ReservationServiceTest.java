package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.entity.*;
import com.somos.airlineticketsservice.repository.CustomerRepository;
import com.somos.airlineticketsservice.repository.ReservationRepository;
import com.somos.airlineticketsservice.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Customer customer;
    private Seat seat1;
    private Seat seat2;
    private Level level;
    private PriceTier priceTier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize customer
        customer = new Customer();
        customer.setEmail("test@example.com");

        // Initialize level and price tier to avoid null pointers
        level = new Level();
        level.setLevelName("First Class");

        priceTier = new PriceTier();
        priceTier.setPrice(100.0);

        // Initialize seats
        seat1 = new Seat();
        seat1.setId(1L);
        seat1.setSeatNumber("A1");
        seat1.setStatus(SeatStatus.HELD);
        seat1.setCustomer(customer);
        seat1.setPriceTier(priceTier);
        seat1.setLevel(level);

        seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("A2");
        seat2.setStatus(SeatStatus.HELD);
        seat2.setCustomer(customer);
        seat2.setPriceTier(priceTier);
        seat2.setLevel(level);
    }

    @Test
    void testReserveHoldSeats_Success() {
        // Arrange
        String[] seatHoldIds = {"1", "2"};

        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(seatRepository.findAllById(anyList())).thenReturn(Arrays.asList(seat1, seat2));

        // Act
        Map<String, Object> response = reservationService.reserveHoldSeats(seatHoldIds, "test@example.com");

        // Assert
        assertNotNull(response);
        assertEquals(2, response.get("numSeats"));
        assertEquals("test@example.com", response.get("customerEmail"));
        verify(seatRepository, times(1)).saveAll(anyList());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testReserveSeats_Success() {
        // Arrange
        String customerEmail = "test@example.com";
        int numSeats = 2;
        int minPrice = 50;
        int maxPrice = 200;
        Optional<String[]> levelNames = Optional.of(new String[]{"First Class"});

        List<Seat> availableSeats = Arrays.asList(seat1, seat2);

        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));
        when(seatRepository.findAvailableSeatsByPriceRangeAndLevels(minPrice, maxPrice, Arrays.asList(levelNames.get()))).thenReturn(availableSeats);

        // Act
        Map<String, Object> response = reservationService.reserveSeats(numSeats, customerEmail, minPrice, maxPrice, levelNames);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.get("numSeats"));
        verify(seatRepository, times(1)).saveAll(anyList());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testGetAllReservations_Success() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setSeats(List.of(seat1));
        reservation.setReservationCode("ABC123");

        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act
        List<Map<String, Object>> reservations = reservationService.getAllReservations();

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        assertEquals("ABC123", reservations.get(0).get("reservationCode"));
    }
}