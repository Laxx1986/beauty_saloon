package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT b FROM Booking b WHERE b.saloonService.serviceProvider.serviceProviderId = :serviceProviderId AND b.date = :date")
    List<Booking> findByServiceProviderAndDate(@Param("serviceProviderId") UUID serviceProviderId, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.saloonService.serviceProvider.serviceProviderId = :serviceProviderId AND b.date = :date AND b.time = :time")
    List<Booking> findByServiceProviderAndDateAndTime(@Param("serviceProviderId") UUID serviceProviderId, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Query("SELECT b.bookingId, b.date, b.time, b.comment, " +
            "u.userName, " +
            "sp.serviceProviderName, " +
            "ss.serviceName, " +
            "sl.serviceLength " +
            "FROM Booking b " +
            "JOIN b.user u " +
            "JOIN b.saloonService ss " +
            "JOIN ss.serviceLength sl " +
            "JOIN ss.serviceProvider sp ")
    List<Object[]> findAllBooking();

    @Query("SELECT ot FROM OpeningTime ot WHERE ot.serviceProvider.serviceProviderId = :serviceProviderId AND ot.date = :dayOfWeek")
    Optional<OpeningTime> findByServiceProviderAndDayOfWeek(@Param("serviceProviderId") UUID serviceProviderId, @Param("dayOfWeek") LocalDate dayOfWeek);
}
