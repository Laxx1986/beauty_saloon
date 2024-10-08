// UserDTO.java
package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.UserRights;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private UUID userId;
    private String userName;
    private String role;

    private String name;
    private String email;
    private String phoneNumber;
    private List<UUID> bookingIds;
    private String password;
    private UserRights userRights;

    private boolean loggedIn = false;

    public UserDTO(UUID userId, String username, String name, String email, String role) {
        this.userId = userId;
        this.userName = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
