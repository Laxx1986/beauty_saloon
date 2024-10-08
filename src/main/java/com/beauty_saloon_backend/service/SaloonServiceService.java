    package com.beauty_saloon_backend.service;

    import com.beauty_saloon_backend.dto.SaloonServiceDTO;
    import com.beauty_saloon_backend.model.SaloonService;
    import com.beauty_saloon_backend.model.ServiceLength;
    import com.beauty_saloon_backend.repository.SaloonServiceRepository;
    import com.beauty_saloon_backend.repository.ServiceLengthRepository;
    import com.beauty_saloon_backend.repository.ServiceProviderRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;
    import java.util.stream.Collectors;

    @Service
    public class SaloonServiceService {

        private final SaloonServiceRepository serviceRepository;
        private final ServiceProviderRepository serviceProviderRepository;
        private final ServiceLengthRepository serviceLengthRepository;



        @Autowired
        public SaloonServiceService(SaloonServiceRepository serviceRepository,
                                    ServiceLengthRepository serviceLengthRepository,
                                    ServiceProviderRepository serviceProviderRepository) {
            this.serviceRepository = serviceRepository;
            this.serviceLengthRepository = serviceLengthRepository;
            this.serviceProviderRepository = serviceProviderRepository;
        }

        public List<Object> getAllServiceDetails() {
            return serviceRepository.findAll()
                    .stream()
                    .map(this::createServiceDetailsObject)
                    .collect(Collectors.toList());
        }

        private Object createServiceDetailsObject(SaloonService saloonService) {
            Map<String, Object> serviceDetails = new HashMap<>();
            serviceDetails.put("serviceId", saloonService.getServiceId());
            serviceDetails.put("serviceName", saloonService.getServiceName());
            serviceDetails.put("servicePrice", saloonService.getServicePrice());
            serviceDetails.put("serviceLength", saloonService.getServiceLength().getServiceLengthId());
            serviceDetails.put("serviceProvider", saloonService.getServiceProvider().getServiceProviderId());

            return serviceDetails;
        }

        public List<Object> getAllServiceDetailsWithNames() {
            return serviceRepository.findAll()
                    .stream()
                    .map(this::createServiceDetailsObjectWithNames)
                    .collect(Collectors.toList());
        }

        private Object createServiceDetailsObjectWithNames(SaloonService saloonService) {
            Map<String, Object> serviceDetails = new HashMap<>();
            serviceDetails.put("serviceId", saloonService.getServiceId());
            serviceDetails.put("serviceName", saloonService.getServiceName());
            serviceDetails.put("servicePrice", saloonService.getServicePrice());
            serviceDetails.put("serviceLength", saloonService.getServiceLength().getServiceLength());
            serviceDetails.put("serviceProvider", saloonService.getServiceProvider().getServiceProviderName());
            return serviceDetails;
        }

        public void addService(SaloonServiceDTO saloonServiceDTO) {
            SaloonService saloonService = new SaloonService();
            saloonService.setServiceName(saloonServiceDTO.getServiceName());
            saloonService.setServicePrice(saloonServiceDTO.getServicePrice());
            saloonService.setServiceLength(serviceLengthRepository.findById(saloonServiceDTO.getServiceLengthId()).orElseThrow());
            saloonService.setServiceProvider(serviceProviderRepository.findById(saloonServiceDTO.getServiceProviderId()).orElseThrow());

            serviceRepository.save(saloonService);
        }
        public void updateService(UUID serviceId, SaloonServiceDTO saloonServiceDTO) {
            SaloonService existingService = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            ServiceLength serviceLength = serviceLengthRepository.findById(saloonServiceDTO.getServiceLengthId())
                    .orElseThrow(() -> new RuntimeException("Service length not found"));

            existingService.setServicePrice(saloonServiceDTO.getServicePrice());
            existingService.setServiceLength(serviceLength);

            serviceRepository.save(existingService);
        }

        public void deleteService(UUID serviceId) {
            SaloonService existingService = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            serviceRepository.delete(existingService);
        }

        private Object createServiceDetailsObjectWithNamesForTable(SaloonService saloonService) {
            Map<String, Object> serviceDetails = new HashMap<>();
            serviceDetails.put("serviceId", saloonService.getServiceId());
            serviceDetails.put("serviceName", saloonService.getServiceName());
            serviceDetails.put("servicePrice", saloonService.getServicePrice());
            serviceDetails.put("serviceLength", saloonService.getServiceLength().getServiceLength());
            serviceDetails.put("serviceProvider", saloonService.getServiceProvider().getServiceProviderName());
            serviceDetails.put("serviceProviderId", saloonService.getServiceProvider().getServiceProviderId());
            return serviceDetails;
        }

        public List<Object> getAllServiceDetailsWithNamesForTable() {
            return serviceRepository.findAll()
                    .stream()
                    .map(this::createServiceDetailsObjectWithNamesForTable)
                    .collect(Collectors.toList());
        }

    }
