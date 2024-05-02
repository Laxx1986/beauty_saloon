package com.beauty_saloon_backend.dto;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.ServiceProvider;
import com.beauty_saloon_backend.model.UserRights;
import lombok.*;

import java.util.List;

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
    private List<Long> bookingIds;
    private String password;
    private UserRights userRights;


}
