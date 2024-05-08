package com.beauty_saloon_backend.repository;

import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.ServiceProvider;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b.bookingId, b.user.userName, b.saloonService.serviceName, b.date, b.time,   b.comment " +
            "FROM Booking b")
    List<Object[]> findAllBooking();



}
