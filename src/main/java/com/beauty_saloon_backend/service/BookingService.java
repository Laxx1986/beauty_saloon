package com.beauty_saloon_backend.service;// BookingService.java

import com.beauty_saloon_backend.converter.BookingConverter;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.repository.BookingRepository;
import com.beauty_saloon_backend.repository.OpeningTimeRepository;
import com.beauty_saloon_backend.repository.ServiceRepository;
import com.beauty_saloon_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingConverter bookingConverter;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository saloonServiceRepository;
    private final OpeningTimeRepository openingTimeRepository;

    @Autowired
    public BookingService(BookingConverter bookingConverter, BookingRepository bookingRepository,
                          UserRepository userRepository, ServiceRepository saloonServiceRepository,
                          OpeningTimeRepository openingTimeRepository) {
        this.bookingConverter = bookingConverter;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.saloonServiceRepository = saloonServiceRepository;
        this.openingTimeRepository = openingTimeRepository;
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

    public ResponseEntity<String> createBooking(BookingDTO bookingDTO) {
        try {
            // Ellenőrizzük, hogy van-e már foglalás a megadott időpontban a szolgáltatónál
            List<Booking> existingBookings = bookingRepository.findByServiceProviderAndDateAndTime(
                    bookingDTO.getServiceId(),
                    new Date(bookingDTO.getDate().getTime()),
                    bookingDTO.getTime());
            if (!existingBookings.isEmpty()) {
                throw new Exception("Time slot already booked");
            }

            // Ellenőrizzük, hogy a foglalás a nyitvatartási időn belül van-e
            String dayOfWeek = new SimpleDateFormat("EEEE").format(bookingDTO.getDate());
            Optional<OpeningTime> openingTime = openingTimeRepository.findByServiceProviderAndDayOfWeek(
                    bookingDTO.getServiceId(), dayOfWeek);
            if (openingTime.isPresent()) {
                Time open = openingTime.get().getTimeFrom();
                Time close = openingTime.get().getTimeTo();
                if (bookingDTO.getTime().before(open) || bookingDTO.getTime().after(close)) {
                    throw new Exception("Booking time is outside of service provider's working hours");
                }
            } else {
                throw new Exception("Service provider's working hours not found");
            }

            // Új foglalás létrehozása és mentése
            Booking booking = bookingConverter.toEntity(bookingDTO,
                    saloonServiceRepository.findById(bookingDTO.getServiceId()).orElseThrow(() -> new Exception("Service not found")),
                    openingTime.get());

            bookingRepository.save(booking);

            return ResponseEntity.ok("Booking created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
