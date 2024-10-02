package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.dto.LevelDTO;
import com.somos.airlineticketsservice.dto.PriceTierDTO;
import com.somos.airlineticketsservice.entity.Level;
import com.somos.airlineticketsservice.entity.PriceTier;
import com.somos.airlineticketsservice.exception.LevelNotFoundException;
import com.somos.airlineticketsservice.repository.LevelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelServiceTest {

    @Mock
    private LevelRepository levelRepository;

    @InjectMocks
    private LevelService levelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLevels() {
        // Given
        Level level1 = createLevel(1L, "Economy", 10, 4);
        Level level2 = createLevel(2L, "Business", 5, 4);

        when(levelRepository.findAll()).thenReturn(Arrays.asList(level1, level2));

        // When
        List<LevelDTO> result = levelService.getAllLevels();

        // Then
        assertEquals(2, result.size());
        assertEquals("Economy", result.get(0).getLevelName());
        assertEquals("Business", result.get(1).getLevelName());
        verify(levelRepository, times(1)).findAll();
    }

    @Test
    void testGetLevelById_WhenExists() {
        // Given
        Level level = createLevel(1L, "Economy", 10, 4);
        when(levelRepository.findById(1L)).thenReturn(Optional.of(level));

        // When
        LevelDTO result = levelService.getLevelById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Economy", result.getLevelName());
        assertEquals(10, result.getRows());
        verify(levelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLevelById_WhenNotFound() {
        // Given
        when(levelRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(LevelNotFoundException.class, () -> levelService.getLevelById(1L));
        verify(levelRepository, times(1)).findById(1L);
    }

    private Level createLevel(Long id, String levelName, int rows, int seatsInRow) {
        Level level = new Level();
        level.setId(id);
        level.setLevelName(levelName);
        level.setRows(rows);
        level.setSeatsInRow(seatsInRow);

        PriceTier priceTier1 = new PriceTier();
        priceTier1.setId(1L);
        priceTier1.setMinSeats(1);
        priceTier1.setMaxSeats(10);
        priceTier1.setPrice(100.0);

        PriceTier priceTier2 = new PriceTier();
        priceTier2.setId(2L);
        priceTier2.setMinSeats(11);
        priceTier2.setMaxSeats(20);
        priceTier2.setPrice(200.0);

        level.setPriceTiers(Arrays.asList(priceTier1, priceTier2));
        return level;
    }
}