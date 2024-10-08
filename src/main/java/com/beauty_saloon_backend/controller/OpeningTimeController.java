package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.service.BookingService;
import com.beauty_saloon_backend.service.OpeningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping("/{selectedServiceProvider}/opening-times")
    public ResponseEntity<List<OpeningTimeDTO>> getOpeningTimesByServiceProvider(@PathVariable UUID selectedServiceProvider) {
        List<OpeningTimeDTO> openingTimes = openingTimeService.getOpeningTimesByServiceProvider(selectedServiceProvider);
        return ResponseEntity.ok(openingTimes);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOpeningTimes(@RequestBody OpeningTimeDTO openingTimeDTO) {
        List<LocalDate> selectedDates = openingTimeDTO.getSelectedDates(); // Use selectedDates from DTO

        if (selectedDates == null || selectedDates.isEmpty()) {
            return ResponseEntity.badRequest().body("No dates selected.");
        }

        for (LocalDate date : selectedDates) {
            openingTimeService.createOpeningTime(openingTimeDTO, date);
        }

        return ResponseEntity.ok("Opening times created successfully.");
    }


}
