package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.ServiceConverter;
import com.beauty_saloon_backend.converter.ServiceLengthConverter;
import com.beauty_saloon_backend.dto.ServiceLengthDTO;
import com.beauty_saloon_backend.model.ServiceLength;
import com.beauty_saloon_backend.repository.ServiceLengthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public ServiceLengthDTO addServiceLength(ServiceLengthDTO serviceLengthDTO) {
        // Ellenőrizzük, hogy létezik-e már a szolgáltatás hossz
        Optional<ServiceLength> existingLength = serviceLengthRepository.findByServiceLength(serviceLengthDTO.getServiceLength());

        if (existingLength.isPresent()) {
            // Ha már létezik, kivételt dobunk, amit a controller kezel
            throw new IllegalArgumentException("Ilyen időtartam már létezik.");
        }

        // Ha nem létezik, létrehozzuk és elmentjük az új szolgáltatási hossz értéket
        ServiceLength serviceLength = new ServiceLength();
        serviceLength.setServiceLength(serviceLengthDTO.getServiceLength());
        ServiceLength savedServiceLength = serviceLengthRepository.save(serviceLength);

        return serviceLengthConverter.toDto(savedServiceLength);
    }

    public void deleteServiceLength(UUID serviceLengthId) {
        ServiceLength serviceLength = serviceLengthRepository.findByServiceLengthId(serviceLengthId);
        if (serviceLength != null) {
            serviceLengthRepository.delete(serviceLength);
        } else {
            throw new IllegalArgumentException("Service length with ID " + serviceLengthId + " not found.");
        }
    }
}
