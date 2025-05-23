package com.beauty_saloon_backend.service;

import com.beauty_saloon_backend.dto.UpdateUserDTO;
import com.beauty_saloon_backend.dto.UserDTO;
import com.beauty_saloon_backend.converter.UserConverter;
import com.beauty_saloon_backend.exceptions.EmailAlreadyExistsException;
import com.beauty_saloon_backend.exceptions.UsernameAlreadyExistsException;
import com.beauty_saloon_backend.model.Booking;
import com.beauty_saloon_backend.model.User;
import com.beauty_saloon_backend.model.UserRights;
import com.beauty_saloon_backend.repository.BookingRepository;
import com.beauty_saloon_backend.repository.UserRepository;
import com.beauty_saloon_backend.repository.UserRightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserRightsRepository userRightsRepository;
    private final UUID defaultUserId = UUID.fromString("46e6fda9-32ad-4489-b1e8-c7694164aa2c");

    @Autowired
    private BookingRepository bookingRepository;


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
        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        User defaultUser = userRepository.findById(defaultUserId)
                .orElseThrow(() -> new RuntimeException("Default user not found"));

        List<Booking> bookings = bookingRepository.findAllByUser_UserId(userId);
        for (Booking booking : bookings) {
            booking.setUser(defaultUser);
        }
        bookingRepository.saveAll(bookings);

        // Töröljük a felhasználót
        userRepository.deleteById(userId);
    }

    public void updateUser(UUID userId, UpdateUserDTO dto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(existingUser);
    }

    public Optional<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.ofNullable(userRepository.findByUserName(username));
    }
}
