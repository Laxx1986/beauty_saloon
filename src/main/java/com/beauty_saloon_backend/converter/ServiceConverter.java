package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceDTO;
import com.beauty_saloon_backend.model.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceConverter {
    public ServiceDTO toDto (Service service) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceId(service.getServiceId());
        serviceDTO.setServiceName(service.getServiceName());
        serviceDTO.setLength(service.getLength());
        serviceDTO.setServicePrice(service.getServicePrice());
        serviceDTO.setServiceProviderId(service.getServiceProviderId());
        return serviceDTO;
    }

    public Service toEntity (ServiceDTO dto) {
        Service service = new Service();
        service.setServiceId(dto.getServiceId());
        service.setServiceName(dto.getServiceName());
        service.setLength(dto.getLength());
        service.setServicePrice(dto.getServicePrice());
        service.setServiceProviderId(dto.getServiceProviderId());
        return service;
    }
}
