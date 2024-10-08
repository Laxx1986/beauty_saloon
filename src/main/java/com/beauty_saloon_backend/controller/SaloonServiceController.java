package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.SaloonServiceDTO;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.SaloonServiceRepository;
import com.beauty_saloon_backend.service.SaloonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class SaloonServiceController {
    private final SaloonServiceService saloonServiceService;
    private final SaloonServiceRepository saloonServiceRepository;

    @Autowired
    public SaloonServiceController(SaloonServiceService saloonServiceService, SaloonServiceRepository saloonServiceRepository) {
        this.saloonServiceService = saloonServiceService;
        this.saloonServiceRepository = saloonServiceRepository;
    }

    @GetMapping("/all-service")
    public ResponseEntity<List<Object>> findAllServiceDetails() {
        List<Object> serviceDetails = saloonServiceService.getAllServiceDetails()
                .stream()
                .map(this::createServiceResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDetails);
    }

    private Object createServiceResponse(Object serviceObject) {
        if (serviceObject instanceof SaloonService) {
            SaloonService service = (SaloonService) serviceObject;
            Map<String, Object> serviceResponse = new HashMap<>();
            serviceResponse.put("serviceId", service.getServiceId());
            serviceResponse.put("serviceName", service.getServiceName());
            serviceResponse.put("servicePrice", service.getServicePrice());
            serviceResponse.put("serviceLength", service.getServiceLength().getServiceLengthId());
            serviceResponse.put("serviceProvider", service.getServiceProvider().getServiceProviderId());

            return serviceResponse;
        } else {
             return serviceObject;
        }
    }

    @GetMapping("/all-service-with-names")
    public ResponseEntity<List<Object>> getAllServiceDetails() {
        List<Object> services = saloonServiceService.getAllServiceDetailsWithNames();
        return ResponseEntity.ok(services);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addService(@RequestBody SaloonServiceDTO saloonServiceDTO) {
        try {
            saloonServiceService.addService(saloonServiceDTO);
            return ResponseEntity.ok("Szolgáltatás sikeresen hozzáadva.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba a szolgáltatás hozzáadásakor: " + e.getMessage());
        }
    }
    @PutMapping("/update/{serviceId}")
    public ResponseEntity<String> updateService(@PathVariable UUID serviceId, @RequestBody SaloonServiceDTO saloonServiceDTO) {
        try {
            saloonServiceService.updateService(serviceId, saloonServiceDTO);
            return ResponseEntity.ok("Szolgáltatás sikeresen módosítva.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hiba történt a szolgáltatás módosításakor: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable UUID serviceId) {
        try {
            saloonServiceService.deleteService(serviceId);
            return ResponseEntity.ok("Szolgáltatás sikeresen törölve.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hiba történt a szolgáltatás törlésekor: " + e.getMessage());
        }
    }

    @GetMapping("/all-service-with-names-for-table")
    public ResponseEntity<List<Object>> createServiceDetailsObjectWithNamesForTable() {
        List<Object> services = saloonServiceService.getAllServiceDetailsWithNamesForTable();
        System.out.println(services);
        return ResponseEntity.ok(services);
    }
}
