package com.beauty_saloon_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private long bookingId;
    private Set<Long> userIds;
    private Long serviceId;
    private Long openingTimeId;
    private Date date;
    private Timestamp time;
    private String comment;
}
