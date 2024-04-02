package com.beauty_saloon_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ServiceDTO {
    private long serviceId;
    private String serviceName;
    private Timestamp serviceLength;
    private int servicePrice;
}
