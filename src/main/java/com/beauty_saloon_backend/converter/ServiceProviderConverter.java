package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceProviderDTO;
import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderConverter {

    public ServiceProviderDTO toDTO(ServiceProvider serviceProvider) {
        ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO();
        serviceProviderDTO.setServiceProviderId(serviceProvider.getServiceProviderId());
        serviceProviderDTO.setServiceProviderName(serviceProvider.getServiceProviderName());
        serviceProviderDTO.setUserId(serviceProvider.getUser().getUserId());
        if (serviceProvider.getUser() != null) {
            serviceProviderDTO.setName(serviceProvider.getUser().getName());
        }
        return serviceProviderDTO;
    }

    public ServiceProvider toEntity(ServiceProviderDTO dto) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setServiceProviderId(dto.getServiceProviderId());
        serviceProvider.setServiceProviderName(dto.getServiceProviderName());
        return serviceProvider;
    }
}
