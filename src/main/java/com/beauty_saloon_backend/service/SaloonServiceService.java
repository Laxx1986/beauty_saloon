    package com.beauty_saloon_backend.service;

    import com.beauty_saloon_backend.model.SaloonService;
    import com.beauty_saloon_backend.repository.SaloonServiceRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Service
    public class SaloonServiceService {

        private final SaloonServiceRepository serviceRepository;

        @Autowired
        public SaloonServiceService(SaloonServiceRepository serviceRepository) {
            this.serviceRepository = serviceRepository;
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

    }
