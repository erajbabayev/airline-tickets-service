package com.somos.airlineticketsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "price_tier_id", nullable = false)
    private PriceTier priceTier;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "hold_time", nullable = true)
    private LocalDateTime holdTime;
}