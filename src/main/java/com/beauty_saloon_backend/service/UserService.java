// UserService.java
package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.converter.UserConverter;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
    }

    public User registerUser(UserDTO userDTO) {
        User user = userConverter.toEntity(userDTO);
        return userRepository.save(user);
    }

    // UserService.java
    public UserDTO loginUser(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName());
        if (user != null && user.getPassword().equals(userDTO.getPassword())) {
            user.setLoggedIn(true);
            userRepository.save(user);
            return UserDTO.builder()
                    .userName(user.getUserName())
                    .userRights(user.getUserRights())
                    .build();
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void logoutUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            user.setLoggedIn(false);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
