package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ServiceLengthDTO;
import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.ServiceLength;
import com.beauty_saloon_backend.service.ServiceLengthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/serviceLengths")
public class ServiceLengthController {
    private ServiceLengthService serviceLengthService;

    @Autowired
    public ServiceLengthController (ServiceLengthService serviceLengthService) {
        this.serviceLengthService = serviceLengthService;
    }

    @GetMapping
    public List<ServiceLengthDTO> getAllServiceLengths() {
        return serviceLengthService.getAllServiceLengths();
    }
}
