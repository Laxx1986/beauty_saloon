package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.UserRights;
import com.beauty_saloon_backend.repository.UserRightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter {

    private final UserRightsRepository userRightsRepository;

    @Autowired
    public UserConverter(UserRightsRepository userRightsRepository) {
        this.userRightsRepository = userRightsRepository;
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .userRights(user.getUserRights())
                .bookingIds(user.getBookings().stream().map(Booking::getBookingId).collect(Collectors.toList()))
                .loggedIn(user.isLoggedIn())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        UserRights defaultUserRights = userRightsRepository.findById(3L).orElseThrow(() -> new RuntimeException("Default UserRights not found"));
        boolean defaultLoggedIn = false; // Alapértelmezett loggedIn érték

        // Az alapértelmezett értékek használata, ha a userDTO-ban nincsenek megadva
        User user = User.builder()
                .userName(userDTO.getUserName())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .userRights(defaultUserRights)
                .loggedIn(defaultLoggedIn)
                .build();

        return user;
    }
}
