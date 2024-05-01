package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.ServiceConverter;
import com.beauty_saloon_backend.converter.ServiceLengthConverter;
import com.beauty_saloon_backend.dto.ServiceLengthDTO;
import com.beauty_saloon_backend.model.ServiceLength;
import com.beauty_saloon_backend.repository.ServiceLengthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceLengthService {

    private final ServiceLengthConverter serviceLengthConverter;
    private final ServiceLengthRepository serviceLengthRepository;
    @Autowired
    public ServiceLengthService (ServiceLengthConverter serviceLengthConverter, ServiceLengthRepository serviceLengthRepository) {
        this.serviceLengthConverter = serviceLengthConverter;
        this.serviceLengthRepository = serviceLengthRepository;
    }

    public List<ServiceLengthDTO> getAllServiceLengths() {
        List<ServiceLength> serviceLengths = serviceLengthRepository.findAll();
        return serviceLengths.stream()
                .map(serviceLengthConverter::toDto)
                .collect(Collectors.toList());
    }

}
