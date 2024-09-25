package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.ServiceDetailsDTO;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.SaloonServiceRepository;
import com.beauty_saloon_backend.service.SaloonServiceService;
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
    public ResponseEntity<List<ServiceDetailsDTO>> getAllServiceDetails() {
        List<ServiceDetailsDTO> services = saloonServiceRepository.findAllServiceDetailsWithNames();
        return ResponseEntity.ok(services);
    }

}
