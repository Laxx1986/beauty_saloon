package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceProviderWithUserDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderWithUserConverter {

    public ServiceProviderWithUserDTO toDTO(ServiceProvider serviceProvider) {

        return ServiceProviderWithUserDTO.builder()
                .serviceProviderName(serviceProvider.getServiceProviderName())
                .userName(serviceProvider.getUser().getUsername())
                .name(serviceProvider.getUser().getName())
                .build();
    }
}
