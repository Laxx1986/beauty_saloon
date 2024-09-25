package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.OpeningTimeDTO;
import com.beauty_saloon_backend.model.OpeningTime;
import org.springframework.stereotype.Component;

@Component
public class OpeningTimeConverter {
    public OpeningTimeDTO toDto (OpeningTime openingTime) {
        OpeningTimeDTO openingTimeDTO = new OpeningTimeDTO();
        openingTimeDTO.setOpeningTimeId(openingTime.getOpeningTimeId());
        openingTimeDTO.setDate(openingTime.getDate());
        openingTimeDTO.setTimeFrom(openingTime.getTimeFrom());
        openingTimeDTO.setTimeTo(openingTime.getTimeTo());
        openingTimeDTO.setServiceProviderId(openingTime.getServiceProvider().getServiceProviderId());
        openingTimeDTO.setUserName(openingTime.getServiceProvider().getUser().getUsername());
        openingTimeDTO.setName(openingTime.getServiceProvider().getUser().getName());
        return openingTimeDTO;
    }

    public OpeningTime toEntity (OpeningTimeDTO openingTimeDTO) {
        OpeningTime openingTime = new OpeningTime();
        openingTime.setOpeningTimeId(openingTimeDTO.getOpeningTimeId());
        openingTime.setDate(openingTimeDTO.getDate());
        openingTime.setTimeFrom(openingTimeDTO.getTimeFrom());
        openingTime.setTimeTo(openingTimeDTO.getTimeTo());
        return openingTime;
    }
}
