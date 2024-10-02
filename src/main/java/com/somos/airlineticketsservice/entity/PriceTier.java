package com.somos.airlineticketsservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int minSeats;

    private int maxSeats;

    private double price;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    @JsonIgnore
    private Level level;
}