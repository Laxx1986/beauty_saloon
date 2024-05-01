package com.beauty_saloon_backend.dto;

import com.beauty_saloon_backend.model.UserRights;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRightsDTO {

    private long userRightsId;
    private String userRightsName;
    private UserRights userRights;


}
