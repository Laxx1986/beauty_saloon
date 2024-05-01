package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ServiceDTO;
import com.beauty_saloon_backend.service.SaloonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class SaloonServiceController {
    private final SaloonServiceService saloonServiceService;

    @Autowired
    public SaloonServiceController (SaloonServiceService saloonServiceService) {
        this.saloonServiceService = saloonServiceService;
    }

    @GetMapping
    public List<ServiceDTO> getAllServices() {
        return saloonServiceService.getAllServices();
    }
}
