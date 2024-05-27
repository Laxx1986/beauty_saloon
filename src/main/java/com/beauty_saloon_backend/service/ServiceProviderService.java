package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;

    @Autowired
    public ServiceProviderService(ServiceProviderRepository serviceProviderRepository) {
        this.serviceProviderRepository = serviceProviderRepository;
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
        // további részletek hozzáadása szükség esetén

        // Például: serviceProviderDetails.put("userDetails", serviceProvider.getUser()); // ez a felhasználó részleteit tartalmazza

        return serviceProviderDetails;
    }
}
