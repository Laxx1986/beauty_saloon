package com.beauty_saloon_backend.dto;

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
}
