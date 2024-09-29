package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.exceptions.EmailAlreadyExistsException;
import com.beauty_saloon_backend.exceptions.UsernameAlreadyExistsException;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.repository.UserRepository;
import com.beauty_saloon_backend.service.EmailService; // Importáljuk az EmailService-t
import com.beauty_saloon_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService; // EmailService példány

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService; // EmailService injektálása
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        try {
            userService.registerUser(userDto);
            // Sikeres regisztráció esetén küldjünk egy emailt
            emailService.sendRegistrationEmail(userDto.getEmail(), userDto.getName()); // Email küldése
            return ResponseEntity.ok("Sikeres regisztráció");
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("A felhasználónév már foglalt"));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Az email cím már foglalt"));
        } catch (IOException e) {
            // Ha email küldés közben hiba történik, adjunk vissza egy megfelelő választ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Sikeres regisztráció, de az email küldése nem sikerült"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Ismeretlen hiba történt"));
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }



    @DeleteMapping("/{userId:[0-9a-fA-F-]{36}}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rights")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserRights(@RequestParam String username) {
        String userRights = userService.getUserRightsByUsername(username);
        return ResponseEntity.ok(userRights);
    }

    @PutMapping("/update/{userId}")
    public void updateUser(@PathVariable UUID userId, @RequestBody User updatedUser) {
        userService.updateUser(userId, updatedUser);
    }
}
