package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.ServiceProvider;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OpeningTimeDTO {
    private UUID openingTimeId;
    private LocalDate date;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private UUID serviceProviderId;

}
