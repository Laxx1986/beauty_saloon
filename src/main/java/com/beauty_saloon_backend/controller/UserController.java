// UserController.java
package com.beauty_saloon_backend.controller;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return users;
    }
}
