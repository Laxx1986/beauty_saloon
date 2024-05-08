package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.ServiceProviderConverter;
import com.beauty_saloon_backend.dto.ServiceProviderDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderConverter serviceProviderConverter;

    @Autowired
    public ServiceProviderService (ServiceProviderRepository serviceProviderRepository, ServiceProviderConverter serviceProviderConverter) {
        this.serviceProviderConverter = serviceProviderConverter;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public List<ServiceProviderDTO> getAllServiceProvider() {
        List <ServiceProvider> serviceProviders= serviceProviderRepository.findAll();
        return serviceProviders.stream()
                .map(serviceProviderConverter::toDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> findServiceProviderAndUserDetails() {
        return serviceProviderRepository.findServiceProviderAndUserDetails();
    }
}
