package com.beauty_saloon_backend.dto;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long userId;
    private String userName;
    private String email;
    private String phoneNumber;
}
