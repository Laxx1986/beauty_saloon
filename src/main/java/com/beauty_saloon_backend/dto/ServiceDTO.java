package com.beauty_saloon_backend.dto;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ServiceDTO {
    private long serviceId;
    private String serviceName;
    private Timestamp serviceLength;
    private int servicePrice;
}
