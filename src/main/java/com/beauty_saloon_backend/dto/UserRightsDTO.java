package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.UserRights;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRightsDTO {

    private UUID userRightsId;
    private String userRightsName;
    private UserRights userRights;


}
