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

    private String seatNumber;  // e.g., "1A", "1B"

    @Enumerated(EnumType.STRING)
    private SeatStatus status;  // AVAILABLE, HELD, BOOKED, etc.

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;  // links to Level entity

    @ManyToOne
    @JoinColumn(name = "price_tier_id", nullable = false)
    private PriceTier priceTier;  // links to PriceTier entity

    @ManyToOne
    @JoinColumn(name = "customer_id")  // Add a foreign key reference to Customer
    private Customer customer;  // Reference to the customer who holds the seat

    @Column(name = "hold_time", nullable = true)
    private LocalDateTime holdTime;  // Time when the seat was held
}