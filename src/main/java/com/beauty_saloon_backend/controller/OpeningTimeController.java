package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.service.OpeningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/all-opening-times-with-users")
    public ResponseEntity<List<Map<String, Object>>> getAllOpeningTimesWithUsers() {
        List<Map<String, Object>> openingTimesWithUsers = openingTimeService.getAllOpeningTimesWithUsers();
        return ResponseEntity.ok(openingTimesWithUsers);
    }
}
