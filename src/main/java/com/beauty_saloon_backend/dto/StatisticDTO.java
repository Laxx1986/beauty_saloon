package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.BookingInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticDTO {
    private String serviceProviderName;
    private List<BookingInfo> bookings;
    private double totalIncome;
}
