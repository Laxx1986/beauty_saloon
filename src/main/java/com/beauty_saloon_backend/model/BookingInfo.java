package com.beauty_saloon_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingInfo {
    private String userName;
    private String serviceName;
    private int bookingMonth;
    private int bookingDay;
    private double servicePrice;
}
