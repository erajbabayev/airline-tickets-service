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

    private String levelName;

    private int rows;

    private int seatsInRow;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PriceTier> priceTiers;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;
}