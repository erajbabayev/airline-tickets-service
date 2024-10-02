package com.somos.airlineticketsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SeatHold {
    private Long seatId;
    private String seatNumber;
    private Double price;
    private String customerEmail;
    private LocalDateTime holdTime;
}