package com.beauty_saloon_backend.dto;
import lombok.*;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO {
    private long serviceProviderId;
    private String serviceProviderName;
}
