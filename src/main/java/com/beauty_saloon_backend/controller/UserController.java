package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.exceptions.EmailAlreadyExistsException;
import com.beauty_saloon_backend.exceptions.UsernameAlreadyExistsException;
import com.beauty_saloon_backend.repository.UserRepository;
import com.beauty_saloon_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;



    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers(); //
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        try {
            // A felhasználó regisztrációjának megkísérlése
            userService.registerUser(userDto);
            // Sikeres regisztráció esetén visszaadjuk a megfelelő választ
            return ResponseEntity.ok("Sikeres regisztráció");
        } catch (UsernameAlreadyExistsException e) {
            // Ha a felhasználónév már foglalt, akkor 400-as státuszkóddal visszaadunk egy hibaüzenetet
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("A felhasználónév már foglalt"));
        } catch (EmailAlreadyExistsException e) {
            // Ha az email cím már foglalt, akkor 400-as státuszkóddal visszaadunk egy hibaüzenetet
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Az email cím már foglalt"));
        } catch (Exception e) {
            // Ismeretlen hiba esetén visszaadunk egy általános hibaüzenetet
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Ismeretlen hiba történt"));
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getter metódus
        public String getMessage() {
            return message;
        }
    }

    @DeleteMapping("/{userId:[0-9a-fA-F-]{36}}")
    @PreAuthorize("hasRole('ADMIN')") // Csak adminisztrátorok számára
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

}
