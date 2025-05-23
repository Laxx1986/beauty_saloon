package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.model.*;
import org.springframework.stereotype.Component;
import com.beauty_saloon_backend.dto.BookingDTO;

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
        dto.setServiceProviderID(booking.getServiceProvider().getServiceProviderId()); // A kapcsolaton keresztül kérjük le
        dto.setConfirmed(booking.isConfirmed());
        dto.setName(booking.getUser().getName());
        dto.setServiceName(booking.getSaloonService().getServiceName());
        if (booking.getSaloonService() != null && booking.getSaloonService().getServiceLength() != null) {
            dto.setServiceLength(booking.getSaloonService().getServiceLength().getServiceLength());
        } else {
            dto.setServiceLength(0);
        }
        return dto;
    }

    public Booking toEntity(BookingDTO bookingDTO, SaloonService saloonService, OpeningTime openingTime, User user, ServiceProvider serviceProvider) {
        Booking booking = new Booking();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setSaloonService(saloonService);
        booking.setDate(bookingDTO.getDate()); // LocalDate
        booking.setTime(bookingDTO.getTime()); // LocalTime
        booking.setComment(bookingDTO.getComment());
        booking.setServiceProvider(serviceProvider);
        booking.setUser(user);
        booking.setConfirmed(bookingDTO.isConfirmed());
        return booking;
    }
}

