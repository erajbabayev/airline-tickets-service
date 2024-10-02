package com.somos.airlineticketsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class AirlineTicketsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineTicketsServiceApplication.class, args);
    }

}
