package com.beauty_saloon_backend.dto;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SaloonServiceDTO {
    private UUID serviceId;
    private String serviceName;
    private UUID serviceLengthId;
    private int servicePrice;
    private UUID serviceProviderId;
    private int manualServiceLength;
}
