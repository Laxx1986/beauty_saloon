package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.ServiceLengthDTO;
import com.beauty_saloon_backend.model.ServiceLength;
import org.springframework.stereotype.Component;

@Component
public class ServiceLengthConverter {
    public ServiceLengthDTO toDto (ServiceLength serviceLength) {
        ServiceLengthDTO serviceLengthDTO = new ServiceLengthDTO();
        serviceLengthDTO.setServiceLengthId(serviceLength.getServiceLengthId());
        serviceLengthDTO.setServiceLength(serviceLength.getServiceLength());
        return serviceLengthDTO;
    }

    public ServiceLength toEntity (ServiceLengthDTO dto) {
        ServiceLength serviceLength = new ServiceLength();
        serviceLength.setServiceLengthId(dto.getServiceLengthId());
        serviceLength.setServiceLength(dto.getServiceLength());
        return serviceLength;
    }
}