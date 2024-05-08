package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.service.SaloonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/services")
public class SaloonServiceController {
    private final SaloonServiceService saloonServiceService;

    @Autowired
    public SaloonServiceController (SaloonServiceService saloonServiceService) {
        this.saloonServiceService = saloonServiceService;
    }

    @GetMapping("/all-service")
    public ResponseEntity<List<Object[]>> findServiceDetails() {
        List<Object[]> serviceDTOS = saloonServiceService.findServiceDetails();
        return ResponseEntity.ok(serviceDTOS);
    }
}
