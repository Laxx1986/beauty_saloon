package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ServiceProviderWithUserDTO;
import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/serviceProviders")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ServiceProviderController (ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping("/all-serviceprovider")
    public ResponseEntity<List<Object>> findAllServiceProviderDetails() {
        List<Object> serviceProviderDetails = serviceProviderService.getAllServiceProviderDetails()
                .stream()
                .map(this::createServiceProviderResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceProviderDetails);
    }

    private Object createServiceProviderResponse(Object serviceProviderObject) {
        if (serviceProviderObject instanceof ServiceProvider) {
            ServiceProvider serviceProvider = (ServiceProvider) serviceProviderObject;
            Map<String, Object> serviceProviderResponse = new HashMap<>();
            serviceProviderResponse.put("serviceProviderId", serviceProvider.getServiceProviderId());
            serviceProviderResponse.put("serviceProviderName", serviceProvider.getServiceProviderName());


            return serviceProviderResponse;
        } else {
            return serviceProviderObject;
        }
    }

    @GetMapping("/with-users")
    public ResponseEntity<List<ServiceProviderWithUserDTO>> findAllServiceProvidersWithUsers() {
        List<ServiceProviderWithUserDTO> serviceProviderDetails = serviceProviderService.getAllServiceProvidersWithUsers();
        return ResponseEntity.ok(serviceProviderDetails);
    }
}
