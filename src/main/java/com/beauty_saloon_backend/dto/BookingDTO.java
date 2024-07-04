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
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private UUID bookingId;
    private UUID userId;
    private UUID serviceId;
    private UUID openingTimeId;
    private Date date;
    private Time time;
    private String comment;
}
