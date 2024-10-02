package com.somos.airlineticketsservice.controller;

import com.somos.airlineticketsservice.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<Map<String, Object>> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/reserveHoldSeats")
    public Map<String, Object> reserveHoldSeats(@RequestParam String[] seatHoldIds, @RequestParam String customerEmail) {
        return reservationService.reserveHoldSeats(seatHoldIds, customerEmail);
    }


    @PostMapping("/reserveSeats")
    public Map<String, Object> reserveSeats(
            @RequestParam int numSeats,
            @RequestParam String customerEmail,
            @RequestParam int minPrice,
            @RequestParam int maxPrice,
            @RequestParam(required = false) String[] levelNames // Optional parameter
    ) {
        return reservationService.reserveSeats(numSeats, customerEmail, minPrice, maxPrice, Optional.ofNullable(levelNames));
    }
}