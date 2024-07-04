package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.SaloonService;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO {
    private UUID serviceProviderId;
    private String serviceProviderName;
    private UUID userId;
}
