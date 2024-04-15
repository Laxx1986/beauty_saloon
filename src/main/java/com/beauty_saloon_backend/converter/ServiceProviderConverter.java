package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceProviderDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderConverter {

    public ServiceProviderDTO toDTO(ServiceProvider serviceProvider) {
        ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO();
        serviceProviderDTO.setServiceProviderId(serviceProvider.getServiceProviderId());
        serviceProviderDTO.setServiceProviderName(serviceProvider.getServiceProviderName());
        serviceProviderDTO.setServices(serviceProvider.getServices());
        return serviceProviderDTO;
    }

    public ServiceProvider toEntity (ServiceProviderDTO dto) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setServiceProviderId(dto.getServiceProviderId());
        serviceProvider.setServiceProviderName(dto.getServiceProviderName());
        serviceProvider.setServices(dto.getServices());
        return serviceProvider;
    }
}
