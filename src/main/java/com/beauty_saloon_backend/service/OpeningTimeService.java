package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.OpeningTimeConverter;
import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.OpeningTimeRepository;
import com.beauty_saloon_backend.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OpeningTimeService {

    private final OpeningTimeConverter openingTimeConverter;
    private final OpeningTimeRepository openingTimeRepository;
    private final ServiceProviderRepository serviceProviderRepository;


    public OpeningTimeService (OpeningTimeRepository openingTimeRepository, OpeningTimeConverter openingTimeConverter, ServiceProviderRepository serviceProviderRepository){
        this.openingTimeConverter = openingTimeConverter;
        this.openingTimeRepository = openingTimeRepository;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public List<OpeningTimeDTO> getAllOpeningTime () {
        List<OpeningTime> openingTimes = openingTimeRepository.findAll();
        return openingTimes.stream()
                .map(openingTimeConverter::toDto)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllOpeningTimesWithUsers() {
        List<OpeningTime> openingTimes = openingTimeRepository.findAll();
        return openingTimes.stream()
                .map(openingTime -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("date", openingTime.getDate());
                    result.put("timeFrom", openingTime.getTimeFrom());
                    result.put("timeTo", openingTime.getTimeTo());
                    result.put("userName", openingTime.getServiceProvider().getUser().getUsername());
                    result.put("name", openingTime.getServiceProvider().getUser().getName());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<OpeningTimeDTO> getOpeningTimesByServiceProvider(UUID selectedServiceProvider) {
        List<OpeningTime> openingTimes = openingTimeRepository.findByServiceProvider_ServiceProviderId(selectedServiceProvider);
        return openingTimes.stream().map(openingTime -> {
            OpeningTimeDTO dto = new OpeningTimeDTO();
            dto.setOpeningTimeId(openingTime.getOpeningTimeId());
            dto.setServiceProviderId(openingTime.getServiceProvider().getServiceProviderId());
            dto.setTimeFrom(openingTime.getTimeFrom());
            dto.setTimeTo(openingTime.getTimeTo());
            dto.setDate(openingTime.getDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public void createOpeningTime(OpeningTimeDTO openingTimeDTO, LocalDate date) {
        OpeningTime openingTime = new OpeningTime();
        openingTime.setServiceProvider(serviceProviderRepository.findById(openingTimeDTO.getServiceProviderId()).orElseThrow());
        openingTime.setDate(date);
        openingTime.setTimeFrom(openingTimeDTO.getTimeFrom());
        openingTime.setTimeTo(openingTimeDTO.getTimeTo());

        // Save the opening time
        openingTimeRepository.save(openingTime);
    }
}
