package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ServiceLengthDTO;
import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.ServiceLength;
import com.beauty_saloon_backend.service.ServiceLengthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/serviceLengths")
public class ServiceLengthController {
    private ServiceLengthService serviceLengthService;

    @Autowired
    public ServiceLengthController (ServiceLengthService serviceLengthService) {
        this.serviceLengthService = serviceLengthService;
    }

    @GetMapping ("/getlengths")
    public List<ServiceLengthDTO> getAllServiceLengths() {
        return serviceLengthService.getAllServiceLengths();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addServiceLength(@RequestBody ServiceLengthDTO serviceLengthDTO) {
        try {
            ServiceLengthDTO savedLength = serviceLengthService.addServiceLength(serviceLengthDTO);
            return ResponseEntity.ok(savedLength);
        } catch (IllegalArgumentException e) {
            // Ha már létezik a hossz, Bad Request választ küldünk
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{serviceLengthId}")
    public void deleteServiceLength(@PathVariable UUID serviceLengthId) {
        serviceLengthService.deleteServiceLength(serviceLengthId);
    }
}
