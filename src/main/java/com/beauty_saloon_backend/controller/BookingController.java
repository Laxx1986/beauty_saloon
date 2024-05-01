package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    @Autowired
    public BookingController (BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDTO> getAllBooking () {
        bookingService.getAllBooking();
    }
}
