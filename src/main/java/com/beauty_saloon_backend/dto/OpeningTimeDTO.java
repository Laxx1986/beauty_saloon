package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.ServiceProvider;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OpeningTimeDTO {
    private long openingTimeId;
    private Timestamp date;
    private Timestamp timeFrom;
    private Timestamp timeTo;
    private ServiceProvider serviceProvider;

}
