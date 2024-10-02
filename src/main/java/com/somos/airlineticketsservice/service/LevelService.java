package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.dto.LevelDTO;
import com.somos.airlineticketsservice.dto.PriceTierDTO;
import com.somos.airlineticketsservice.entity.Level;
import com.somos.airlineticketsservice.entity.PriceTier;
import com.somos.airlineticketsservice.exception.LevelNotFoundException;
import com.somos.airlineticketsservice.repository.LevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelService {

    private final LevelRepository levelRepository;

    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public List<LevelDTO> getAllLevels() {
        List<Level> levels = levelRepository.findAll();

        return levels.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LevelDTO getLevelById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new LevelNotFoundException(id));

        return convertToDto(level);
    }

    private LevelDTO convertToDto(Level level) {
        LevelDTO levelDTO = new LevelDTO();
        levelDTO.setId(level.getId());
        levelDTO.setLevelName(level.getLevelName());
        levelDTO.setRows(level.getRows());
        levelDTO.setSeatsInRow(level.getSeatsInRow());

        List<PriceTierDTO> priceTierDTOs = level.getPriceTiers().stream()
                .map(this::convertPriceTierToDto)
                .collect(Collectors.toList());

        levelDTO.setPriceTiers(priceTierDTOs);
        return levelDTO;
    }

    private PriceTierDTO convertPriceTierToDto(PriceTier priceTier) {
        PriceTierDTO dto = new PriceTierDTO();
        dto.setId(priceTier.getId());
        dto.setMinSeats(priceTier.getMinSeats());
        dto.setMaxSeats(priceTier.getMaxSeats());
        dto.setPrice(priceTier.getPrice());
        return dto;
    }
}