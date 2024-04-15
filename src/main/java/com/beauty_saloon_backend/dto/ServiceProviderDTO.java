package com.beauty_saloon_backend.dto;
import com.beauty_saloon_backend.model.Service;
import lombok.*;

import java.util.List;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO {
    private long serviceProviderId;
    private String serviceProviderName;
    private List<Service> services;
}
