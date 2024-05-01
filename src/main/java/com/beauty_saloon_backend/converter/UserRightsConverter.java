package com.beauty_saloon_backend.converter;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.dto.UserRightsDTO;
import com.beauty_saloon_backend.model.UserRights;
import org.springframework.stereotype.Component;

@Component
public class UserRightsConverter {

    public UserRightsDTO toDTO (UserRights userRights) {
        UserRightsDTO userRightsDTO = new UserRightsDTO();
        userRightsDTO.setUserRightsId(userRights.getUserRightsId());
        userRightsDTO.setUserRightsName(userRights.getUserRightsName());
        return userRightsDTO;
    }

    public UserRights toUserRights (UserRightsDTO userRightsDTO) {
        UserRights userRights = new UserRights();
        userRights.setUserRightsId(userRightsDTO.getUserRightsId());
        userRights.setUserRightsName(userRights.getUserRightsName());
        return userRights;
    }
}
