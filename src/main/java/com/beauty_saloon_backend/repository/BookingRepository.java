package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.ServiceProvider;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Transactional
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.saloonService.serviceProvider.serviceProviderId = :serviceProviderId AND b.date = :date AND b.time = :time")
    List<Booking> findByServiceProviderAndDateAndTime(@Param("serviceProviderId") Long serviceProviderId, @Param("date") Date date, @Param("time") Time time);

    @Query("SELECT b.bookingId, b.date, b.time, b.comment, " +
            "u.userName, " +
            "sp.serviceProviderName, " +
            "ss.serviceName, " +
            "sl.serviceLength " +
            "FROM Booking b " +
            "JOIN b.user u " +
            "JOIN b.saloonService ss " +
            "JOIN ss.serviceLength sl " +
            "JOIN ss.serviceProvider sp " +
            "JOIN b.openingTime ot")
    List<Object[]> findAllBooking();


}
