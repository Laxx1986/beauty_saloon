// UserConverter.java
package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.Booking;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .userRights(user.getUserRights())
                .bookingIds(user.getBookings().stream().map(Booking::getBookingId).collect(Collectors.toList()))
                .build();
    }

    public User toEntity(UserDTO dto) {
        return User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(dto.getPassword())
                .userRights(dto.getUserRights())
                .build();
    }
}
