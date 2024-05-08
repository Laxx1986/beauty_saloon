package com.beauty_saloon_backend.service;
import com.beauty_saloon_backend.model.SaloonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beauty_saloon_backend.converter.ServiceConverter;
import com.beauty_saloon_backend.dto.ServiceDTO;
import com.beauty_saloon_backend.repository.ServiceRepository;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SaloonServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceConverter serviceConverter;
    @Autowired
    public SaloonServiceService (ServiceRepository serviceRepository, ServiceConverter serviceConverter) {
        this.serviceConverter = serviceConverter;
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceDTO> getAllServices() {
        List<SaloonService> services = serviceRepository.findAll();
        return services.stream()
                .map(serviceConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<Object[]> findServiceDetails() {
        return serviceRepository.findServiceDetails();
    }
}
