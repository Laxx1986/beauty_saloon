package com.beauty_saloon_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OpeningTimeDTO {
    private long openingTimeId;
    private Timestamp date;
    private Timestamp timeFrom;
    private Timestamp timeTo;
}
