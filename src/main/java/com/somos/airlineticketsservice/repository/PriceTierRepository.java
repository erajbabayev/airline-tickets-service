package com.somos.airlineticketsservice.repository;

import com.somos.airlineticketsservice.entity.PriceTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceTierRepository extends JpaRepository<PriceTier, Long> {
}