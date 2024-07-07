package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.BookingConverter;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.repository.BookingRepository;
import com.beauty_saloon_backend.repository.OpeningTimeRepository;
import com.beauty_saloon_backend.repository.ServiceRepository;
import com.beauty_saloon_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class BookingService {

    private final BookingConverter bookingConverter;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository saloonServiceRepository;
    private final OpeningTimeRepository openingTimeRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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

    @Transactional
    public ResponseEntity<String> createBooking(BookingDTO bookingDTO) {
        try {
            UUID serviceId = bookingDTO.getServiceId();
            SaloonService saloonService = saloonServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            int serviceLength = saloonService.getServiceLength().getServiceLength(); // Időtartam percben

            // Ellenőrizzük, hogy van-e már foglalás a megadott időpontban a szolgáltatónál
            List<Booking> existingBookings = bookingRepository.findByServiceProviderAndDate(
                    bookingDTO.getServiceProviderID(),
                    bookingDTO.getDate()
            );

            LocalTime requestedStartTime = bookingDTO.getTime();
            LocalTime requestedEndTime = requestedStartTime.plusMinutes(serviceLength);

            for (Booking existingBooking : existingBookings) {
                LocalTime existingStartTime = existingBooking.getTime();
                LocalTime existingEndTime = existingStartTime.plusMinutes(existingBooking.getSaloonService().getServiceLength().getServiceLength());

                // Ellenőrizzük, hogy az új foglalás kezdő és vég időpontja ne essen az már meglévő foglalás időtartományába
                if (!(requestedEndTime.isBefore(existingStartTime) || requestedStartTime.isAfter(existingEndTime))) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Time slot already booked");
                }
            }

            // Ellenőrizzük, hogy a foglalás a nyitvatartási időn belül van-e
            Optional<OpeningTime> openingTime = openingTimeRepository.findByServiceProviderAndDate(
                    bookingDTO.getServiceProviderID(), bookingDTO.getDate());
            logger.info("Searching for OpeningTime with serviceProviderId: {} and date: {}", bookingDTO.getServiceProviderID(), bookingDTO.getDate());

            if (openingTime.isPresent()) {
                LocalTime open = openingTime.get().getTimeFrom();
                LocalTime close = openingTime.get().getTimeTo();
                if (requestedStartTime.isBefore(open) || requestedEndTime.isAfter(close)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking time is outside of service provider's working hours");
                }
            } else {
                logger.warn("Service provider's working hours not found for serviceProviderId: {} and date: {}", bookingDTO.getServiceProviderID(), bookingDTO.getDate());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service provider's working hours not found");
            }

            // Felhasználó betöltése
            User user = userRepository.findById(bookingDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

            // Új foglalás létrehozása és mentése
            Booking booking = bookingConverter.toEntity(bookingDTO, saloonService, openingTime.get(), user);

            bookingRepository.save(booking);

            return ResponseEntity.ok("Booking created successfully");
        } catch (Exception e) {
            // Logoljuk a kivételt a diagnosztika érdekében
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the booking: " + e.getMessage());
        }
    }

}
