package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.dto.AvailableSeats;
import com.somos.airlineticketsservice.dto.SeatHold;
import com.somos.airlineticketsservice.entity.Customer;
import com.somos.airlineticketsservice.entity.Seat;
import com.somos.airlineticketsservice.entity.SeatStatus;
import com.somos.airlineticketsservice.repository.CustomerRepository;
import com.somos.airlineticketsservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;

    @Value("${seat.hold.expiration.seconds:120}")
    private int holdExpirationSeconds;

    public SeatService(SeatRepository seatRepository, CustomerRepository customerRepository) {
        this.seatRepository = seatRepository;
        this.customerRepository = customerRepository;
    }

    public List<AvailableSeats> findAvailableSeats(Optional<String[]> levelNames) {
        releaseExpiredHolds();

        List<Object[]> seatData;
        if (levelNames.isPresent() && levelNames.get().length > 0) {
            seatData = seatRepository.findAvailableSeatsByLevelNames(levelNames.get());
        } else {
            seatData = seatRepository.findAvailableSeatsForAllLevels();
        }

        Map<String, Map<Double, Long>> levelPriceMap = new LinkedHashMap<>();
        Map<String, Long> levelSeatCountMap = new LinkedHashMap<>();

        for (Object[] record : seatData) {
            String levelName = (String) record[0];
            Long seatCount = (Long) record[1];
            Double price = (Double) record[2];

            levelPriceMap.computeIfAbsent(levelName, k -> new TreeMap<>()).merge(price, seatCount, Long::sum);
            levelSeatCountMap.merge(levelName, seatCount, Long::sum);
        }

        DecimalFormat priceFormatter = new DecimalFormat("#.00");

        return levelPriceMap.entrySet().stream()
                .map(entry -> {
                    String levelName = entry.getKey();
                    Long availableSeatsCount = levelSeatCountMap.get(levelName);

                    List<String> priceBreakdown = entry.getValue().entrySet().stream()
                            .map(e -> "Price: " + priceFormatter.format(e.getKey()) + "$, Available seats: " + e.getValue())
                            .collect(Collectors.toList());

                    return new AvailableSeats(levelName, availableSeatsCount, priceBreakdown);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public SeatHold[] findAndHoldSeats(int numSeats, String customerEmail) {
        releaseExpiredHolds();
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<Seat> availableSeats = seatRepository.findAvailableSeatsForFirstClass();

        if (availableSeats.size() < numSeats) {
            throw new IllegalArgumentException("Not enough seats available in First Class");
        }

        List<Seat> seatsToHold = availableSeats.subList(0, numSeats);
        LocalDateTime holdTime = LocalDateTime.now();

        seatsToHold.forEach(seat -> {
            seat.setStatus(SeatStatus.HELD);
            seat.setCustomer(customer);
            seat.setHoldTime(holdTime);
        });

        seatRepository.saveAll(seatsToHold);

        return seatsToHold.stream()
                .map(seat -> new SeatHold(seat.getId(), seat.getSeatNumber(), seat.getPriceTier().getPrice(), customerEmail, holdTime))
                .toArray(SeatHold[]::new);
    }

    public void releaseExpiredHolds() {
        List<Seat> heldSeats = seatRepository.findHeldSeats();
        LocalDateTime now = LocalDateTime.now();

        heldSeats.forEach(seat -> {
            if (seat.getHoldTime() != null && seat.getHoldTime().plusSeconds(holdExpirationSeconds).isBefore(now)) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setCustomer(null);
                seat.setHoldTime(null);
            }
        });

        seatRepository.saveAll(heldSeats);
    }

    public List<Map<String, Object>> getHeldSeatsWithRemainingTime() {
        releaseExpiredHolds();
        List<Seat> heldSeats = seatRepository.findHeldSeats();
        LocalDateTime now = LocalDateTime.now();

        return heldSeats.stream().map(seat -> {
            Map<String, Object> seatInfo = new HashMap<>();
            seatInfo.put("seatNumber", seat.getSeatNumber());
            seatInfo.put("customerEmail", seat.getCustomer().getEmail());

            long elapsedTime = java.time.Duration.between(seat.getHoldTime(), now).getSeconds();
            long remainingTime = holdExpirationSeconds - elapsedTime;

            seatInfo.put("remainingHoldTime", Math.max(remainingTime, 0));
            return seatInfo;
        }).collect(Collectors.toList());
    }
}