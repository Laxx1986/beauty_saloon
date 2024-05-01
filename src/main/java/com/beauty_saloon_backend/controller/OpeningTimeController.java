package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.service.OpeningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/openingTimes")
public class OpeningTimeController {
    private final OpeningTimeService openingTimeService;

    @Autowired
    public OpeningTimeController (OpeningTimeService openingTimeService) {
        this.openingTimeService = openingTimeService;
    }

    @GetMapping
    public List<OpeningTimeDTO> getAllOpeningTime () {
        return openingTimeService.getAllOpeningTime();
    }
}
