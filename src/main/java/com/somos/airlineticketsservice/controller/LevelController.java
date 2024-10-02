package com.somos.airlineticketsservice.controller;

import com.somos.airlineticketsservice.dto.LevelDTO;
import com.somos.airlineticketsservice.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LevelController {

    private final LevelService levelService;

    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("/levels")
    public List<LevelDTO> getAllLevels() {
        return levelService.getAllLevels();
    }
}