package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.ServiceLength;
import com.beauty_saloon_backend.model.ServiceProvider;
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
    private ServiceLength length;
    private int servicePrice;
    private ServiceProvider serviceProviderId;
}
