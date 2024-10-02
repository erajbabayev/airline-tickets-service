package com.somos.airlineticketsservice.dto;

import lombok.Data;

@Data
public class PriceTierDTO {
    private Long id;
    private int minSeats;
    private int maxSeats;
    private double price;
}
