package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.SaloonService;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO {
    private long serviceProviderId;
    private String serviceProviderName;
    private Long userId;
}
