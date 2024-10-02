package com.somos.airlineticketsservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class LevelDTO {
    private Long id;
    private String levelName;
    private int rows;
    private int seatsInRow;
    private List<PriceTierDTO> priceTiers;
}
