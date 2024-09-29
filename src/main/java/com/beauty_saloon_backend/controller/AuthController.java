package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.model.AuthenticationRequest;
import com.beauty_saloon_backend.model.AuthenticationResponse;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.service.CustomUserDetailsService;
import com.beauty_saloon_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hibás felhasználónév vagy jelszó");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userDetailsService.findUserByUsername(authRequest.getUsername());


        // Kinyerjük a granted authorities-t
        String userRights = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null); // Vagy kezelj le más logikát

        return ResponseEntity.ok(new AuthenticationResponse(jwt, userRights, user.getUserId()));
    }
}
