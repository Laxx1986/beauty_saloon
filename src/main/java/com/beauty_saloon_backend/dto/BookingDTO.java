package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private UUID bookingId;
    private UUID userId;
    private UUID serviceId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm[:ss]")
    private LocalTime time;
    private String comment;
    private UUID serviceProviderID;
    private boolean confirmed = false;
    private String serviceName;
    private String name;
    private String serviceProviderName;
}

