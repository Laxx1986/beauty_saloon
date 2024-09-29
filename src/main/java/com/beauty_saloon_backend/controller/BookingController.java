package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.SaloonServiceRepository;
import com.beauty_saloon_backend.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/service-provider2/{serviceProviderId}")
    public ResponseEntity<List<BookingDTO>> getBookingsForServiceProvider(@PathVariable UUID serviceProviderId) {
        return ResponseEntity.ok(bookingService.getBookingsForServiceProvider(serviceProviderId));
    }

    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<String> confirmBooking(@PathVariable UUID bookingId) {
        bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok("Foglalás sikeresen megerősítve.");
    }

    @PutMapping("/unconfirm/{bookingId}")
    public ResponseEntity<String> unconfirmBooking(@PathVariable UUID bookingId) {
        bookingService.unconfirmBooking(bookingId);
        return ResponseEntity.ok("Foglalás megerősítése visszavonva.");
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Foglalás sikeresen törölve.");
    }

    @PutMapping("/update/{bookingId}")
    public ResponseEntity<String> updateBooking(@PathVariable UUID bookingId, @RequestBody BookingDTO updatedBookingDTO) {
        try {
            bookingService.updateBooking(bookingId, updatedBookingDTO); // Átadjuk a BookingDTO-t a service-nek
            return ResponseEntity.ok("Foglalás sikeresen módosítva.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nem sikerült a foglalás módosítása.");
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable UUID bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
