package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private long bookingId;
    private Long userId;
    private Long serviceId;
    private Long openingTimeId;
    private Date date;
    private Time time;
    private String comment;
    private boolean confirmed;
}
