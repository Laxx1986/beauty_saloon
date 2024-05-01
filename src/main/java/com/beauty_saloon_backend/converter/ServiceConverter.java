package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceDTO;
import com.beauty_saloon_backend.model.SaloonService;
import org.springframework.stereotype.Component;

@Component
public class ServiceConverter {
    public ServiceDTO toDto (SaloonService saloonService) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceId(saloonService.getServiceId());
        serviceDTO.setServiceName(saloonService.getServiceName());
        serviceDTO.setLength(saloonService.getLength());
        serviceDTO.setServicePrice(saloonService.getServicePrice());
        serviceDTO.setServiceProviderId(saloonService.getServiceProviderId());
        return serviceDTO;
    }

    public SaloonService toEntity (ServiceDTO dto) {
        SaloonService saloonService = new SaloonService();
        saloonService.setServiceId(dto.getServiceId());
        saloonService.setServiceName(dto.getServiceName());
        saloonService.setLength(dto.getLength());
        saloonService.setServicePrice(dto.getServicePrice());
        saloonService.setServiceProviderId(dto.getServiceProviderId());
        return saloonService;
    }
}
