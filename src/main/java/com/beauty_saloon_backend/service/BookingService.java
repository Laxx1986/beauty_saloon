// BookingService.java
package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.controller.BookingController;
import com.beauty_saloon_backend.converter.BookingConverter;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingConverter bookingConverter;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService (BookingConverter bookingConverter, BookingRepository bookingRepository) {
        this.bookingConverter = bookingConverter;
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(bookingConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<Object[]> findAllBooking() {
        return bookingRepository.findAllBooking();
    }
}
