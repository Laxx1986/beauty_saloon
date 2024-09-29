package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.BookingConverter;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.repository.BookingRepository;
import com.beauty_saloon_backend.repository.OpeningTimeRepository;
import com.beauty_saloon_backend.repository.SaloonServiceRepository;
import com.beauty_saloon_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final SaloonServiceRepository saloonServiceRepository;
    private final OpeningTimeRepository openingTimeRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public BookingService(BookingConverter bookingConverter, BookingRepository bookingRepository,
                          UserRepository userRepository, SaloonServiceRepository saloonServiceRepository,
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
                    .orElseThrow(() -> new RuntimeException("A szolgáltatás nem található"));
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
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Az idősáv már foglalt");
                }
            }

            // Ellenőrizzük, hogy a foglalás a nyitvatartási időn belül van-e
            Optional<OpeningTime> openingTime = openingTimeRepository.findByServiceProviderAndDate(
                    bookingDTO.getServiceProviderID(), bookingDTO.getDate());
            logger.info("Searching for OpeningTime with serviceProviderId: {} and date: {}", bookingDTO.getServiceProviderID(), bookingDTO.getDate());

            if (openingTime.isPresent()) {
                LocalTime open = openingTime.get().getTimeFrom();
                LocalTime close = openingTime.get().getTimeTo();
                logger.info("Nyitás kezdetet: " + open + " Nyitás vége: " + close);
                if (requestedStartTime.isBefore(open) || requestedEndTime.isAfter(close)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A foglalás a szolgáltató nyitvatartási idején túl van!");
                }
            } else {
                logger.warn("Service provider's working hours not found for serviceProviderId: {} and date: {}", bookingDTO.getServiceProviderID(), bookingDTO.getDate());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nyitvatartási idő nem található");
            }

            // Felhasználó betöltése
            User user = userRepository.findById(bookingDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

            // Új foglalás létrehozása és mentése
            Booking booking = bookingConverter.toEntity(bookingDTO, saloonService, openingTime.get(), user);

            bookingRepository.save(booking);

            return ResponseEntity.ok("Sikeres foglalás");
        } catch (Exception e) {
            // Logoljuk a kivételt a diagnosztika érdekében
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba a foglalás elküldésekor:  " + e.getMessage());
        }
    }

    public List<BookingDTO> getBookingsForServiceProvider(UUID serviceProviderId) {
        // Check if the provider is the super admin (show all bookings)
        List<Booking> bookings;
        if (serviceProviderId.toString().equals("b0315726-766f-47b1-bd85-1e160743c38c")) {
            bookings = bookingRepository.findAll(); // Retrieve all bookings
        } else {
            bookings = bookingRepository.findByServiceProviderID(serviceProviderId);
        }

        // Convert List<Booking> to List<BookingDTO using BookingConverter
        return bookings.stream()
                .map(bookingConverter::toDto) // Használjuk a BookingConverter-t
                .collect(Collectors.toList());
    }

    public void deleteBooking(UUID bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Transactional
    public ResponseEntity<String> updateBooking(UUID bookingId, BookingDTO updatedBookingDTO) {
        logger.info("Tranzakció megkezdődött a foglalás módosításához: {}", bookingId);

        try {
            // Ellenőrizd, hogy a foglalás létezik-e
            logger.info("Foglalás keresése ID alapján: {}", bookingId);
            Booking existingBooking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Foglalás nem található"));
            logger.info("Foglalás megtalálva: {}", existingBooking);

            // Ellenőrizd, hogy a szolgáltatás létezik-e
            logger.info("Szolgáltatás keresése ID alapján: {}", updatedBookingDTO.getServiceId());
            UUID serviceId = updatedBookingDTO.getServiceId();
            SaloonService saloonService = saloonServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Szolgáltatás nem található"));
            int serviceLength = saloonService.getServiceLength().getServiceLength(); // Időtartam percben
            logger.info("Szolgáltatás megtalálva: {}, Időtartam: {}", saloonService.getServiceName(), serviceLength);

            // Ellenőrizzük, hogy van-e már foglalás a megadott időpontban a szolgáltatónál (kivéve a jelenlegi foglalást)
            logger.info("Létező foglalások keresése a szolgáltatónál: {} és dátum: {}", updatedBookingDTO.getServiceProviderID(), updatedBookingDTO.getDate());
            List<Booking> existingBookings = bookingRepository.findByServiceProviderAndDate(
                    updatedBookingDTO.getServiceProviderID(),
                    updatedBookingDTO.getDate()
            );

            LocalTime requestedStartTime = updatedBookingDTO.getTime();
            LocalTime requestedEndTime = requestedStartTime.plusMinutes(serviceLength);
            logger.info("Kért kezdő időpont: {}, Kért befejező időpont: {}", requestedStartTime, requestedEndTime);

            for (Booking booking : existingBookings) {
                if (!booking.getBookingId().equals(bookingId)) { // Kizárjuk az aktuális foglalást
                    LocalTime existingStartTime = booking.getTime();
                    LocalTime existingEndTime = existingStartTime.plusMinutes(booking.getSaloonService().getServiceLength().getServiceLength());
                    logger.info("Létező foglalás kezdő időpont: {}, Vég időpont: {}", existingStartTime, existingEndTime);

                    // Ellenőrizzük, hogy az új foglalás kezdő és vég időpontja ne essen a már meglévő foglalás időtartományába
                    if (!(requestedEndTime.isBefore(existingStartTime) || requestedStartTime.isAfter(existingEndTime))) {
                        logger.warn("Az idősáv már foglalt: kezdő időpont: {}, vég időpont: {}", requestedStartTime, requestedEndTime);
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Az idősáv már foglalt");
                    }
                }
            }

            // Ellenőrizzük, hogy a foglalás a nyitvatartási időn belül van-e
            logger.info("Nyitvatartási idő keresése a szolgáltatónál: {} és dátum: {}", updatedBookingDTO.getServiceProviderID(), updatedBookingDTO.getDate());
            Optional<OpeningTime> openingTime = openingTimeRepository.findByServiceProviderAndDate(
                    updatedBookingDTO.getServiceProviderID(), updatedBookingDTO.getDate());

            if (openingTime.isPresent()) {
                LocalTime open = openingTime.get().getTimeFrom();
                LocalTime close = openingTime.get().getTimeTo();
                logger.info("Nyitvatartási idő: kezdő: {}, záró: {}", open, close);

                if (requestedStartTime.isBefore(open) || requestedEndTime.isAfter(close)) {
                    logger.warn("A foglalás a nyitvatartási időn kívül van: kezdő időpont: {}, záró időpont: {}", requestedStartTime, requestedEndTime);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A foglalás a szolgáltató nyitvatartási idején túl van!");
                }
            } else {
                logger.warn("Nyitvatartási idő nem található a szolgáltatónál: {} és dátum: {}", updatedBookingDTO.getServiceProviderID(), updatedBookingDTO.getDate());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nyitvatartási idő nem található");
            }

            // Frissítsük az aktuális foglalást
            existingBooking.setDate(updatedBookingDTO.getDate());
            existingBooking.setTime(updatedBookingDTO.getTime());
            existingBooking.setComment(updatedBookingDTO.getComment());
            existingBooking.setSaloonService(saloonService); // Új szolgáltatás beállítása

            logger.info("Foglalás frissítése: {}", existingBooking);
            bookingRepository.saveAndFlush(existingBooking);

            logger.info("Foglalás sikeresen frissítve: {}", existingBooking);
            return ResponseEntity.ok("Foglalás sikeresen módosítva.");
        } catch (Exception e) {
            // Logoljuk a kivételt a diagnosztika érdekében
            logger.error("Hiba a foglalás módosításakor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba a foglalás módosításakor: " + e.getMessage());
        }
    }




    public void confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setConfirmed(true);
        bookingRepository.save(booking);
    }

    public void unconfirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setConfirmed(false);
        bookingRepository.save(booking);
    }
}
