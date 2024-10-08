package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.BookingConverter;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.dto.StatisticDTO;
import com.beauty_saloon_backend.model.*;
import com.beauty_saloon_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.beauty_saloon_backend.repository.ServiceProviderRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BookingService {

    private final BookingConverter bookingConverter;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SaloonServiceRepository saloonServiceRepository;
    private final OpeningTimeRepository openingTimeRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final ServiceProviderRepository serviceProviderRepository;
    private final EmailService emailService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public BookingService(BookingConverter bookingConverter,
                          BookingRepository bookingRepository,
                          UserRepository userRepository,
                          SaloonServiceRepository saloonServiceRepository,
                          OpeningTimeRepository openingTimeRepository,
                          ServiceProviderRepository serviceProviderRepository, EmailService emailService) {
        this.bookingConverter = bookingConverter;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.saloonServiceRepository = saloonServiceRepository;
        this.openingTimeRepository = openingTimeRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.emailService = emailService;
    }

    public List<BookingDTO> getAllBookings() {
        Optional<User> currentUser = getCurrentUser();

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            String role = user.getUserRights().getUserRightsName();
            UUID currentUserId = user.getUserId();

            List<Booking> bookings = bookingRepository.findAll();

            return bookings.stream()
                    .map(booking -> {
                        String serviceProviderName = booking.getServiceProvider().getServiceProviderName();

                        if ("User".equals(role) && !booking.getUser().getUserId().equals(currentUserId)) {
                            // Limit the details for bookings not owned by the current user
                            return new BookingDTO(
                                    booking.getBookingId(),
                                    null,
                                    booking.getSaloonService().getServiceId(),
                                    booking.getDate(),
                                    booking.getTime(),
                                    null,
                                    booking.getServiceProvider().getServiceProviderId(),
                                    false,
                                    booking.getSaloonService().getServiceName(),
                                    null,
                                    serviceProviderName // Set the service provider's name
                            );
                        } else {
                            // Return full details
                            return new BookingDTO(
                                    booking.getBookingId(),
                                    booking.getUser().getUserId(),
                                    booking.getSaloonService().getServiceId(),
                                    booking.getDate(),
                                    booking.getTime(),
                                    booking.getComment(),
                                    booking.getServiceProvider().getServiceProviderId(),
                                    booking.isConfirmed(),
                                    booking.getSaloonService().getServiceName(),
                                    booking.getUser().getName(),
                                    serviceProviderName // Set the service provider's name
                            );
                        }
                    })
                    .collect(Collectors.toList());
        }

        return List.of();
    }


    private Optional<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.ofNullable(userRepository.findByUserName(username));
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

            // Ellenőrizzük a ServiceProvider-t
            ServiceProvider serviceProvider = serviceProviderRepository.findById(bookingDTO.getServiceProviderID())
                    .orElseThrow(() -> new RuntimeException("Szolgáltató nem található"));

            int serviceLength = saloonService.getServiceLength().getServiceLength(); // Időtartam percben

            // Ellenőrizzük, hogy van-e már foglalás a megadott időpontban a szolgáltatónál
            List<Booking> existingBookings = bookingRepository.findByServiceProviderAndDate(
                    serviceProvider.getServiceProviderId(),
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
                    serviceProvider.getServiceProviderId(), bookingDTO.getDate());
            logger.info("Searching for OpeningTime with serviceProviderId: {} and date: {}", serviceProvider.getServiceProviderId(), bookingDTO.getDate());

            if (openingTime.isPresent()) {
                LocalTime open = openingTime.get().getTimeFrom();
                LocalTime close = openingTime.get().getTimeTo();
                logger.info("Nyitás kezdetet: " + open + " Nyitás vége: " + close);
                if (requestedStartTime.isBefore(open) || requestedEndTime.isAfter(close)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A foglalás a szolgáltató nyitvatartási idején túl van!");
                }
            } else {
                logger.warn("Service provider's working hours not found for serviceProviderId: {} and date: {}", serviceProvider.getServiceProviderId(), bookingDTO.getDate());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nyitvatartási idő nem található");
            }

            // Felhasználó betöltése
            User user = userRepository.findById(bookingDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

            // Új foglalás létrehozása és mentése
            Booking booking = bookingConverter.toEntity(bookingDTO, saloonService, openingTime.get(), user, serviceProvider);

            bookingRepository.save(booking);
            emailService.sendBookingConfirmationEmail(booking.getUser().getEmail(), booking);


            return ResponseEntity.ok("Sikeres foglalás");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba a foglalás elküldésekor:  " + e.getMessage());
        }
    }


    public void deleteBooking(UUID bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public ResponseEntity<String> updateBooking(UUID bookingId, BookingDTO updatedBookingDTO) {
        logger.info("Tranzakció megkezdődött a foglalás módosításához: {}", bookingId);

        try {
            // Ellenőrizzük, hogy a foglalás létezik-e
            logger.info("Foglalás keresése ID alapján: {}", bookingId);
            Booking existingBooking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Foglalás nem található"));
            logger.info("Foglalás megtalálva: {}", existingBooking);

            // Ellenőrizzük, hogy a szolgáltatás létezik-e
            logger.info("Szolgáltatás keresése ID alapján: {}", updatedBookingDTO.getServiceId());
            UUID serviceId = updatedBookingDTO.getServiceId();
            SaloonService saloonService = saloonServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Szolgáltatás nem található"));
            int serviceLength = saloonService.getServiceLength().getServiceLength(); // Időtartam percben
            logger.info("Szolgáltatás megtalálva: {}, Időtartam: {}", saloonService.getServiceName(), serviceLength);

            // Ha az updatedBookingDTO nem tartalmaz serviceProviderID-t, akkor az existingBooking-ból vesszük
            UUID serviceProviderId = updatedBookingDTO.getServiceProviderID();
            if (serviceProviderId == null) {
                serviceProviderId = existingBooking.getServiceProvider().getServiceProviderId();
                logger.info("ServiceProviderID az existingBooking-ból beállítva: {}", serviceProviderId);
            }

            // Lekérjük a ServiceProvider entitást a repository segítségével
            ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                    .orElseThrow(() -> new RuntimeException("ServiceProvider nem található"));

            // Ellenőrizzük, hogy van-e már foglalás a megadott időpontban a szolgáltatónál (kivéve a jelenlegi foglalást)
            logger.info("Létező foglalások keresése a szolgáltatónál: {} és dátum: {}", serviceProviderId, updatedBookingDTO.getDate());
            List<Booking> existingBookings = bookingRepository.findByServiceProviderAndDate(
                    serviceProviderId,
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
            logger.info("Nyitvatartási idő keresése a szolgáltatónál: {} és dátum: {}", serviceProviderId, updatedBookingDTO.getDate());
            Optional<OpeningTime> openingTime = openingTimeRepository.findByServiceProviderAndDate(
                    serviceProviderId, updatedBookingDTO.getDate());

            if (openingTime.isPresent()) {
                LocalTime open = openingTime.get().getTimeFrom();
                LocalTime close = openingTime.get().getTimeTo();
                logger.info("Nyitvatartási idő: kezdő: {}, záró: {}", open, close);

                if (requestedStartTime.isBefore(open) || requestedEndTime.isAfter(close)) {
                    logger.warn("A foglalás a nyitvatartási időn kívül van: kezdő időpont: {}, záró időpont: {}", requestedStartTime, requestedEndTime);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A foglalás a szolgáltató nyitvatartási idején túl van!");
                }
            } else {
                logger.warn("Nyitvatartási idő nem található a szolgáltatónál: {} és dátum: {}", serviceProviderId, updatedBookingDTO.getDate());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nyitvatartási idő nem található");
            }

            // Frissítsük az aktuális foglalást
            existingBooking.setDate(updatedBookingDTO.getDate());
            existingBooking.setTime(updatedBookingDTO.getTime());
            existingBooking.setComment(updatedBookingDTO.getComment());
            existingBooking.setSaloonService(saloonService);
            existingBooking.setServiceProvider(serviceProvider);
            existingBooking.setConfirmed(false);


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

    public List<BookingDTO> getBookingsForServiceProvider(UUID serviceProviderId) {
        List<Booking> bookings;

        if (serviceProviderId.toString().equals("b0315726-766f-47b1-bd85-1e160743c38c")) {
            // Ha az admin lekérdezi, akkor minden foglalást visszaadunk
            bookings = bookingRepository.findAll();
        } else {
            // Csak az adott szolgáltató foglalásait kérjük le
            bookings = bookingRepository.findByServiceProvider_ServiceProviderId(serviceProviderId);
        }

        // Foglalások DTO-vá alakítása
        return bookings.stream()
                .map(bookingConverter::toDto)
                .collect(Collectors.toList());
    }



    public List<BookingDTO> getBookingsByServiceProviderAndDate(UUID serviceProviderId, LocalDate date) {
        logger.info("Lekérdezendő Service Provider ID: " + serviceProviderId);
        logger.info("Lekérdezendő dátum: " + date);

        // Lekérjük a foglalásokat a repository-ból a szolgáltató ID és a dátum alapján
        List<Booking> bookings = bookingRepository.findByServiceProvider_ServiceProviderIdAndDate(serviceProviderId, date);

        logger.info("Lekérdezett foglalások száma: " + bookings.size());
        logger.info("Lekérdezett foglalások: " + bookings);

        // Mappelés BookingDTO-ra
        return bookings.stream().map(booking -> {
            BookingDTO dto = new BookingDTO();
            dto.setBookingId(booking.getBookingId());
            dto.setServiceProviderID(booking.getServiceProvider().getServiceProviderId());
            dto.setServiceId(booking.getSaloonService().getServiceId());
            dto.setUserId(booking.getUser().getUserId());
            dto.setDate(booking.getDate());
            dto.setTime(booking.getTime());
            dto.setComment(booking.getComment());
            dto.setServiceName(booking.getSaloonService().getServiceName());
            return dto;
        }).collect(Collectors.toList());
    }



    public void confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Foglalás nem található."));

        // Foglalás megerősítése
        booking.setConfirmed(true);
        bookingRepository.save(booking);

        // E-mail küldése a foglalás megerősítésekor
        try {
            emailService.sendBookingConfirmedEmail(
                    booking.getUser().getEmail(),
                    booking.getUser().getName(),
                    booking.getSaloonService().getServiceName(),
                    booking.getDate(),
                    booking.getTime()
            );
        } catch (IOException e) {
            System.err.println("Nem sikerült az email küldése a foglalás megerősítésekor: " + e.getMessage());
        }
    }


    public void unconfirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        booking.setConfirmed(false);
        bookingRepository.save(booking);
    }

    public List<LocalDate> getBookedDatesByServiceProvider(UUID serviceProviderId) {
        List<Booking> bookings = bookingRepository.findByServiceProvider_ServiceProviderId(serviceProviderId);
        return bookings.stream()
                .map(Booking::getDate)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsForUser(UUID userId) {
        List<Booking> userBookings = bookingRepository.findAllByUser_UserId(userId);
        return userBookings.stream()
                .map(bookingConverter::toDto)
                .collect(Collectors.toList());
    }
    public List<StatisticDTO> getMonthlyStatistics(int year, int month) {
        List<Booking> bookings = bookingRepository.findByYearAndMonth(year, month);
        Map<ServiceProvider, List<Booking>> groupedByProvider = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getServiceProvider));

        List<StatisticDTO> statistics = new ArrayList<>();
        for (Map.Entry<ServiceProvider, List<Booking>> entry : groupedByProvider.entrySet()) {
            ServiceProvider provider = entry.getKey();
            List<Booking> providerBookings = entry.getValue();

            double totalIncome = 0;
            List<BookingInfo> bookingInfos = new ArrayList<>();

            for (Booking booking : providerBookings) {
                SaloonService service = booking.getSaloonService();
                double servicePrice = service.getServicePrice();
                totalIncome += servicePrice;

                BookingInfo info = new BookingInfo(
                        booking.getUser().getName(),
                        service.getServiceName(),
                        booking.getDate().getMonthValue(),
                        booking.getDate().getDayOfMonth(),
                        servicePrice
                );

                bookingInfos.add(info);
            }

            StatisticDTO statisticsDTO = new StatisticDTO(provider.getServiceProviderName(), bookingInfos, totalIncome);
            statistics.add(statisticsDTO);
        }

        return statistics;
    }

}
