package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.SaloonServiceRepository;
import com.beauty_saloon_backend.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final SaloonServiceRepository saloonServiceRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingController(BookingService bookingService, SaloonServiceRepository saloonServiceRepository) {
        this.bookingService = bookingService;
        this.saloonServiceRepository = saloonServiceRepository;
    }

    @GetMapping("/all-booking")
    public ResponseEntity<List<Object[]>> findAllBooking() {
        List<Object[]> bookingDTOs = bookingService.findAllBooking();
        return ResponseEntity.ok(bookingDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO) {
        return bookingService.createBooking(bookingDTO);
    }

    @GetMapping("/service-provider/{serviceProviderId}")
    public ResponseEntity<List<SaloonService>> getServicesByServiceProvider(@PathVariable UUID serviceProviderId) {
        logger.info("Received booking data: {}", serviceProviderId);
        List<SaloonService> services = saloonServiceRepository.findByServiceProviderId(serviceProviderId);
        return ResponseEntity.ok(services);
    }
}
