package com.somos.airlineticketsservice.repository;

import com.somos.airlineticketsservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Query to find available seats for all levels sorted by level ID and price
    @Query("SELECT s.level.levelName, COUNT(s), p.price " +
            "FROM Seat s JOIN s.level l JOIN s.priceTier p " +
            "WHERE s.status = 'AVAILABLE' " +
            "GROUP BY s.level.levelName, p.price " +
            "ORDER BY l.id ASC, p.price ASC")
    List<Object[]> findAvailableSeatsForAllLevels();

    // Query to find available seats for specific level names sorted by level ID and price
    @Query("SELECT s.level.levelName, COUNT(s), p.price " +
            "FROM Seat s JOIN s.level l JOIN s.priceTier p " +
            "WHERE s.status = 'AVAILABLE' AND s.level.levelName IN :levelNames " +
            "GROUP BY s.level.levelName, p.price " +
            "ORDER BY l.id ASC, p.price ASC")
    List<Object[]> findAvailableSeatsByLevelNames(String[] levelNames);

    // Query to find available seats specifically for First Class level
    @Query("SELECT s FROM Seat s WHERE s.status = 'AVAILABLE' AND s.level.levelName = 'First Class' ORDER BY s.priceTier.price ASC")
    List<Seat> findAvailableSeatsForFirstClass();

    // Query to find seats that are currently held
    @Query("SELECT s FROM Seat s WHERE s.status = 'HELD'")
    List<Seat> findHeldSeats();

    @Query("SELECT s FROM Seat s " +
            "WHERE s.status = 'AVAILABLE' " +
            "AND s.priceTier.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:levelNames IS NULL OR s.level.levelName IN (:levelNames)) " +
            "AND (:levelNames IS NOT NULL OR s.priceTier.price = " +
            "(SELECT MIN(p.price) FROM PriceTier p WHERE p.price BETWEEN :minPrice AND :maxPrice)) " +
            "ORDER BY s.level.id ASC, s.priceTier.price ASC")
    List<Seat> findAvailableSeatsByPriceRangeAndLevels(int minPrice, int maxPrice, List<String> levelNames);

}