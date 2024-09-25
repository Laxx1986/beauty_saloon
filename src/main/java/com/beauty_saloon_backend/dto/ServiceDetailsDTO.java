package com.beauty_saloon_backend.dto;

import lombok.*;

import java.util.UUID;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailsDTO {
    private UUID serviceId;
    private String serviceName;
    private int servicePrice;
    private int serviceLength;
    private String serviceProviderName;
}
