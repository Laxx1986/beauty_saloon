package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/all-users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDto) {
        try {
            UserDTO loggedInUser = userService.loginUser(userDto);
            loggedInUser.setUserRights(userService.findUserRightsByUsername(userDto.getUserName()));
            return ResponseEntity.ok(loggedInUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody UserDTO userDto) {
        try {
            userService.logoutUser(userDto.getUserName());
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

        @DeleteMapping("/{userId}")
        public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        }
}


