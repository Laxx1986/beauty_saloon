package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.StatisticDTO;
import com.beauty_saloon_backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyStatistics(@RequestParam int year, @RequestParam int month) {
        List<StatisticDTO> statistics = bookingService.getMonthlyStatistics(year, month);
        return ResponseEntity.ok(statistics);
    }
}

