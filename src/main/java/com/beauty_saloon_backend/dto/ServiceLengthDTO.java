package com.beauty_saloon_backend.dto;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLengthDTO {
    private UUID serviceLengthId;
    private int serviceLength;
}

