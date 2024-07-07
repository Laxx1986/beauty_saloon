package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/all-booking")
    public ResponseEntity<List<Object[]>> findAllBooking() {
        List<Object[]> bookingDTOs = bookingService.findAllBooking();
        return ResponseEntity.ok(bookingDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO) {
        logger.info("Received booking data: {}", bookingDTO);
        return bookingService.createBooking(bookingDTO);
    }
}
