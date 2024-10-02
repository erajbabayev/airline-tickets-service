package com.somos.airlineticketsservice.exception;

public class LevelNotFoundException extends RuntimeException {
    public LevelNotFoundException(Long id) {
        super("Level with id " + id + " not found.");
    }

    public LevelNotFoundException(String[] levelNames) {
            super("No matching levels found for: " + String.join(", ", levelNames));
        }

}