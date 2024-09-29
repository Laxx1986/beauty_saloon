package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.ServiceProviderWithUserConverter;
import com.beauty_saloon_backend.dto.ServiceProviderWithUserDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderWithUserConverter serviceProviderWithUserConverter;


    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository,
                                  ServiceProviderWithUserConverter serviceProviderWithUserConverter) {
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceProviderWithUserConverter = serviceProviderWithUserConverter;
    }

    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    public List<Object> getAllServiceProviderDetails() {
        return serviceProviderRepository.findAll()
                .stream()
                .map(this::createServiceProviderDetailsObject)
                .collect(Collectors.toList());
    }

    private Object createServiceProviderDetailsObject(ServiceProvider serviceProvider) {
        Map<String, Object> serviceProviderDetails = new HashMap<>();
        serviceProviderDetails.put("serviceProviderId", serviceProvider.getServiceProviderId());
        serviceProviderDetails.put("serviceProviderName", serviceProvider.getServiceProviderName());
        return serviceProviderDetails;
    }

    public List<ServiceProviderWithUserDTO> getAllServiceProvidersWithUsers() {
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAll();
        return serviceProviders.stream()
                .map(serviceProviderWithUserConverter::toDTO)
                .collect(Collectors.toList());
    }

    public ServiceProvider findByUser_UserId(UUID userId) {
        return serviceProviderRepository.findByUser_UserId(userId);
    }
}
