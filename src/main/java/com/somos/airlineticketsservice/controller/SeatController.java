package com.somos.airlineticketsservice.controller;

import com.somos.airlineticketsservice.dto.AvailableSeats;
import com.somos.airlineticketsservice.dto.SeatHold;
import com.somos.airlineticketsservice.service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/availableSeats")
    public List<AvailableSeats> getAvailableSeats(@RequestParam Optional<String[]> levelNames) {
        return seatService.findAvailableSeats(levelNames);
    }

    /**
     * Find and hold seats for First Class level
     *
     * @param numSeats number of seats to hold
     * @param customerEmail customer's email address
     * @return an array of SeatHold objects representing the held seats
     */
    @PostMapping("/holdSeats")
    public SeatHold[] holdSeats(@RequestParam int numSeats, @RequestParam String customerEmail) {
        return seatService.findAndHoldSeats(numSeats, customerEmail);
    }

    @GetMapping("/heldSeats")
    public List<Map<String, Object>> getHeldSeats() {
        return seatService.getHeldSeatsWithRemainingTime();
    }
}