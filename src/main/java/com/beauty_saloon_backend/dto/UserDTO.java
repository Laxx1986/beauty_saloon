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

    private String name;
    private String email;
    private String phoneNumber;
    private List<UUID> bookingIds;
    private String password;
    private UserRights userRights;

    private boolean loggedIn;
}
