package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.SaloonServiceDTO;
import com.beauty_saloon_backend.model.SaloonService;
import org.springframework.stereotype.Component;

@Component
public class ServiceConverter {
    public SaloonServiceDTO toDto (SaloonService saloonService) {
        SaloonServiceDTO serviceDTO = new SaloonServiceDTO();
        serviceDTO.setServiceId(saloonService.getServiceId());
        serviceDTO.setServiceName(saloonService.getServiceName());
        serviceDTO.setServiceLengthId(saloonService.getServiceLength().getServiceLengthId());
        serviceDTO.setServicePrice(saloonService.getServicePrice());
        serviceDTO.setServiceProviderId(saloonService.getServiceProvider().getServiceProviderId());
        return serviceDTO;
    }

    public SaloonService toEntity (SaloonServiceDTO dto) {
        SaloonService saloonService = new SaloonService();
        saloonService.setServiceId(dto.getServiceId());
        saloonService.setServiceName(dto.getServiceName());
        saloonService.setServicePrice(dto.getServicePrice());
        return saloonService;
    }
}
