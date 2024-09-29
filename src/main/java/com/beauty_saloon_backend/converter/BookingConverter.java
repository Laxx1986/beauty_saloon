package com.beauty_saloon_backend.converter;

import org.springframework.stereotype.Component;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.model.User;

@Component
public class BookingConverter {

    public BookingDTO toDto(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserId(booking.getUser().getUserId());
        dto.setServiceId(booking.getSaloonService().getServiceId());
        dto.setDate(booking.getDate());
        dto.setTime(booking.getTime());
        dto.setComment(booking.getComment());
        dto.setServiceProviderID(booking.getServiceProviderID());
        dto.setConfirmed(booking.isConfirmed());
        dto.setServiceName(booking.getSaloonService().getServiceName());
        return dto;
    }


    public Booking toEntity(BookingDTO bookingDTO, SaloonService saloonService, OpeningTime openingTime, User user) {
        Booking booking = new Booking();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setSaloonService(saloonService);
        booking.setDate(bookingDTO.getDate()); // LocalDate
        booking.setTime(bookingDTO.getTime()); // LocalTime
        booking.setComment(bookingDTO.getComment());
        booking.setServiceProviderID(bookingDTO.getServiceProviderID());
        booking.setUser(user); // Ez a sor hozzáadja a User objektumot a Booking-hez
        booking.setConfirmed(bookingDTO.isConfirmed());
        return booking;
    }
}
