package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.ServiceProvider;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OpeningTimeDTO {
    private UUID openingTimeId;
    private Timestamp date;
    private Time timeFrom;
    private Time timeTo;
    private UUID serviceProviderId;

}
