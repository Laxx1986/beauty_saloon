package com.beauty_saloon_backend.dto;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLengthDTO {
    private long serviceLengthId;
    private int serviceLength;
}

