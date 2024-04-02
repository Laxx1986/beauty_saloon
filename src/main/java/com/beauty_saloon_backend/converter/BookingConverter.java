package com.beauty_saloon_backend.converter;

import org.springframework.stereotype.Component;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.Service;
import com.beauty_saloon_backend.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;


@Component
public class BookingConverter {
    public BookingDTO toDto(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserIds(booking.getUsers().stream().map(User::getUserId).collect(Collectors.toSet()));
        dto.setServiceId(booking.getService().getServiceId());
        dto.setOpeningTimeId(booking.getOpeningTime().getOpeningTimeId());
        dto.setDate(booking.getDate());
        dto.setTime(booking.getTime());
        dto.setComment(booking.getComment());
        return dto;
    }

    public Booking toEntity(BookingDTO bookingDTO, Service service, OpeningTime openingTime) {
        Booking booking = new Booking();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setUsers(new HashSet<>());
        booking.setService(service);
        booking.setOpeningTime(openingTime);
        booking.setDate(bookingDTO.getDate());
        booking.setTime(bookingDTO.getTime());
        booking.setComment(bookingDTO.getComment());
        return booking;
    }
}
