package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.converter.OpeningTimeConverter;
import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.model.OpeningTime;
import com.beauty_saloon_backend.model.SaloonService;
import com.beauty_saloon_backend.repository.OpeningTimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpeningTimeService {

    private final OpeningTimeConverter openingTimeConverter;
    private final OpeningTimeRepository openingTimeRepository;

    public OpeningTimeService (OpeningTimeRepository openingTimeRepository, OpeningTimeConverter openingTimeConverter){
        this.openingTimeConverter = openingTimeConverter;
        this.openingTimeRepository = openingTimeRepository;
    }

    public List<OpeningTimeDTO> getAllOpeningTime () {
        List<OpeningTime> openingTimes = openingTimeRepository.findAll();
        return openingTimes.stream()
                .map(openingTimeConverter::toDto)
                .collect(Collectors.toList());
    }
}
