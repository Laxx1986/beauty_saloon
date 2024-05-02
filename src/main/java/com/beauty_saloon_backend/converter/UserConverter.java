package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.UserRights;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserDTO toDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .userRights(user.getUserRights())
                .bookingIds(user.getBookings().stream().map(Booking::getBookingId).collect(Collectors.toList()))
                .build();

        Hibernate.initialize(user.getBookings());

        return userDTO;
    }

    public User toEntity(UserDTO dto) {
        User user = User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(dto.getPassword())
                .userRights(dto.getUserRights())
                .build();
        return user;
    }
}

