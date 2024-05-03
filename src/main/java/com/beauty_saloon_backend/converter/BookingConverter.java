
package com.beauty_saloon_backend.converter;

import org.springframework.stereotype.Component;
import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;


@Component
public class BookingConverter {
    public BookingDTO toDto(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserId(booking.getUser().getUserId());
        dto.setServiceId(booking.getSaloonService().getServiceId());
        dto.setOpeningTimeId(booking.getOpeningTime().getOpeningTimeId());
        dto.setDate(booking.getDate());
        dto.setTime(booking.getTime());
        dto.setComment(booking.getComment());
        return dto;
    }

    public Booking toEntity(BookingDTO bookingDTO, SaloonService saloonService, OpeningTime openingTime) {
        Booking booking = new Booking();
        booking.setBookingId(bookingDTO.getBookingId());
        booking.setSaloonService(saloonService);
        booking.setOpeningTime(openingTime);
        booking.setDate(bookingDTO.getDate());
        booking.setTime(bookingDTO.getTime());
        booking.setComment(bookingDTO.getComment());
        return booking;
    }
}
