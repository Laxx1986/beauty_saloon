package com.beauty_saloon_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
@AllArgsConstructor
public class UserDTO {
    private long userId;
    private String userName;
    private String email;

    private String phoneNumber;
    private String password;
}
