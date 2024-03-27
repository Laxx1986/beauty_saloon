package com.beauty_saloon_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ServiceDTO {
    private long serviceId;
    private String serviceName;
    private int serviceLength;
    private int servicePrice;
}
