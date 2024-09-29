package com.beauty_saloon_backend.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwt;
    private String userRights;
    private UUID userId;

    public AuthenticationResponse(String jwt, UUID userId, String userRights) {
        this.jwt = jwt;
        this.userId = userId; // Inicializáld a userId-t
        this.userRights = userRights;
    }
}
