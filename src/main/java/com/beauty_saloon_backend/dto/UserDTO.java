// UserDTO.java
package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.UserRights;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private long userId;
    private String userName;

    private String name;
    private String email;
    private String phoneNumber;
    private List<Long> bookingIds;
    private String password;
    private UserRights userRights;

    private boolean loggedIn;
}
