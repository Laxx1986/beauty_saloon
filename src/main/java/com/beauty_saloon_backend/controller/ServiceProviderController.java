package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.BookingDTO;
import com.beauty_saloon_backend.dto.ServiceProviderDTO;
import com.beauty_saloon_backend.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/serviceProviders")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ServiceProviderController (ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @GetMapping("/all-serviceprovider")
    public ResponseEntity<List<ServiceProviderDTO>> getAllServiceProvider() {
        List<ServiceProviderDTO> serviceProviderDTOS = serviceProviderService.getAllServiceProvider();
        return ResponseEntity.ok(serviceProviderDTOS);
    }
}
