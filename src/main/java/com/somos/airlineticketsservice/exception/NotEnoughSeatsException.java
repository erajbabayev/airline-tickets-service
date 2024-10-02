package com.somos.airlineticketsservice.exception;

public class NotEnoughSeatsException extends RuntimeException {
    public NotEnoughSeatsException(int availableSeats, int requestedSeats) {
        super("Not enough seats available. Requested: " + requestedSeats + ", Available: " + availableSeats);
    }
}