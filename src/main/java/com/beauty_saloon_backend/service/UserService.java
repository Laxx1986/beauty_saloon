package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.converter.UserConverter;
import com.beauty_saloon_backend.exceptions.EmailAlreadyExistsException;
import com.beauty_saloon_backend.exceptions.UsernameAlreadyExistsException;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.UserRights;
import com.beauty_saloon_backend.repository.UserRepository;
import com.beauty_saloon_backend.repository.UserRightsRepository;
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
    private final UserRightsRepository userRightsRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder, UserRightsRepository userRightsRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userRightsRepository = userRightsRepository;
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

    public User registerUser(UserDTO userDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if(userRepository.findByUserName(userDTO.getUserName()) != null){
            throw new UsernameAlreadyExistsException("A felhasználónév már foglalt");
        }
        if(userRepository.findByEmail(userDTO.getEmail()) != null){
            throw new EmailAlreadyExistsException("Az email cím már foglalt");
        }

        // Alapértelmezett "User" jogosultság beállítása
        UserRights userRights = userRightsRepository.findByUserRightsName("User");


        User user = userConverter.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Jogosultság hozzáadása a felhasználóhoz
        user.setUserRights(userRights);

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
