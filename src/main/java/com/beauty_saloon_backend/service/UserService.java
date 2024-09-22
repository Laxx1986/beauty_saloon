package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.converter.UserConverter;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.UserRights;
import com.beauty_saloon_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userConverter.toDTO(user);
    }

    public User registerUser(UserDTO userDTO) {
        if(userRepository.findByUserName(userDTO.getUserName()) != null){
            throw new RuntimeException("Username is already taken");
        }
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new RuntimeException("Email is already in use");
        }

        User user = userConverter.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public String getUserRightsByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("A felhasználó nem található!");
        }
        return user.getUserRights().getUserRightsName();
    }

    public void deleteUser(UUID userId) {
        if(!userRepository.existsById(userId)){
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
