package com.somos.airlineticketsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailableSeats {
    private String levelName;
    private Long availableSeatsCount;
    private List<String> priceBreakdown; // List of formatted price and seat details
}