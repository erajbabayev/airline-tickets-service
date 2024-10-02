package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.entity.Customer;
import com.somos.airlineticketsservice.entity.Reservation;
import com.somos.airlineticketsservice.entity.Seat;
import com.somos.airlineticketsservice.entity.SeatStatus;
import com.somos.airlineticketsservice.exception.LevelNotFoundException;
import com.somos.airlineticketsservice.exception.NotEnoughSeatsException;
import com.somos.airlineticketsservice.repository.CustomerRepository;
import com.somos.airlineticketsservice.repository.ReservationRepository;
import com.somos.airlineticketsservice.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(SeatRepository seatRepository, CustomerRepository customerRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Map<String, Object> reserveHoldSeats(String[] seatHoldIds, String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Convert String[] to List<Long>
        List<Long> holdIds = Arrays.stream(seatHoldIds)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // Find the held seats based on the holdIds
        List<Seat> seatsToReserve = seatRepository.findAllById(holdIds)
                .stream()
                .filter(seat -> seat.getStatus() == SeatStatus.HELD && seat.getCustomer().equals(customer))
                .collect(Collectors.toList());

        if (seatsToReserve.size() != seatHoldIds.length) {
            throw new IllegalArgumentException("Some seats are no longer held or not available");
        }

        // Create a reservation and save it
        String reservationCode = UUID.randomUUID().toString();
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setSeats(seatsToReserve);
        reservation.setReservationCode(reservationCode);
        reservation.setReservationTime(LocalDateTime.now());

        // Update seat status to BOOKED
        seatsToReserve.forEach(seat -> seat.setStatus(SeatStatus.RESERVED));

        seatRepository.saveAll(seatsToReserve);
        reservationRepository.save(reservation);

        // Build detailed response
        Map<String, Object> response = new HashMap<>();
        response.put("reservationCode", reservationCode);
        response.put("customerEmail", customerEmail);
        response.put("numSeats", seatsToReserve.size());
        response.put("reservationTime", reservation.getReservationTime());

        // Add seat details
        List<Map<String, Object>> seatDetails = seatsToReserve.stream().map(seat -> {
            Map<String, Object> seatInfo = new HashMap<>();
            seatInfo.put("seatNumber", seat.getSeatNumber());
            seatInfo.put("price", seat.getPriceTier().getPrice());
            seatInfo.put("level", seat.getLevel().getLevelName());
            return seatInfo;
        }).collect(Collectors.toList());

        response.put("seats", seatDetails);

        return response;
    }

    @Transactional
    public Map<String, Object> reserveSeats(int numSeats, String customerEmail, int minPrice, int maxPrice, Optional<String[]> levelNames) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<Seat> availableSeats;

        if (levelNames.isPresent() && levelNames.get().length > 0) {
            availableSeats = seatRepository.findAvailableSeatsByPriceRangeAndLevels(minPrice, maxPrice, Arrays.asList(levelNames.get()));

            if (availableSeats.isEmpty()) {
                throw new LevelNotFoundException(levelNames.get());
            }
        } else {
            // Pass null to the query when no levels are provided, which will match all levels
            availableSeats = seatRepository.findAvailableSeatsByPriceRangeAndLevels(minPrice, maxPrice, null);
        }

        if (availableSeats.size() < numSeats) {
            throw new NotEnoughSeatsException(availableSeats.size(), numSeats);
        }

        List<Seat> seatsToReserve = availableSeats.subList(0, numSeats);
        String reservationCode = UUID.randomUUID().toString();

        // Create a new reservation
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setSeats(seatsToReserve);
        reservation.setReservationCode(reservationCode);
        reservation.setReservationTime(LocalDateTime.now());

        // Mark seats as reserved
        seatsToReserve.forEach(seat -> seat.setStatus(SeatStatus.RESERVED));

        seatRepository.saveAll(seatsToReserve);
        reservationRepository.save(reservation);

        // Build response with reservation details
        Map<String, Object> response = new HashMap<>();
        response.put("reservationCode", reservationCode);
        response.put("numSeats", seatsToReserve.size());

        // Add seat details to the response
        List<Map<String, Object>> seatDetails = seatsToReserve.stream().map(seat -> {
            Map<String, Object> seatInfo = new HashMap<>();
            seatInfo.put("seatNumber", seat.getSeatNumber());
            seatInfo.put("price", seat.getPriceTier().getPrice());
            seatInfo.put("level", seat.getLevel().getLevelName());
            return seatInfo;
        }).collect(Collectors.toList());

        response.put("seats", seatDetails);
        response.put("totalPrice", seatsToReserve.stream().mapToDouble(seat -> seat.getPriceTier().getPrice()).sum());

        return response;
    }

    public List<Map<String, Object>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(reservation -> {
            Map<String, Object> reservationDetails = new HashMap<>();
            reservationDetails.put("reservationCode", reservation.getReservationCode());
            reservationDetails.put("numSeats", reservation.getSeats().size());
            reservationDetails.put("customerEmail", reservation.getCustomer().getEmail());
            reservationDetails.put("reservationTime", reservation.getReservationTime());

            // Collect seat details (seat number, price, and level)
            List<Map<String, Object>> seatDetails = reservation.getSeats().stream().map(seat -> {
                Map<String, Object> seatInfo = new HashMap<>();
                seatInfo.put("seatNumber", seat.getSeatNumber());
                seatInfo.put("price", seat.getPriceTier().getPrice());
                seatInfo.put("level", seat.getLevel().getLevelName());
                return seatInfo;
            }).collect(Collectors.toList());

            reservationDetails.put("seats", seatDetails);

            return reservationDetails;
        }).collect(Collectors.toList());
    }
}