package com.somos.airlineticketsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String levelName;  // e.g., "First Class", "Business"

    private int rows;  // number of rows in the level

    private int seatsInRow;  // seats per row

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PriceTier> priceTiers;  // pricing tiers within the level

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;  // seats within the level
}