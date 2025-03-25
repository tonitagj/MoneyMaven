package org.example.moneymaventonita.controllers;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.moneymaventonita.JwtUtil;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.UsersService;
import org.example.moneymaventonita.services.dtos.UsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
@Slf4j
public class UsersApi {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ User Registration
    @PostMapping("/registration")
    public ResponseEntity<?> saveNewUser(@RequestBody UsersDTO usersDto) {
        log.info("Received registration request: {}", usersDto);

        if (userRepository.findByEmail(usersDto.getEmail()).isPresent()) {
            log.warn("Duplicate email detected: {}", usersDto.getEmail());
            return ResponseEntity.status(409).body("Email already exists! Please use a different email.");
        }

        if (usersService.saveNewUser(usersDto)) {
            return ResponseEntity.status(201).body("User registered successfully!");
        } else {
            return ResponseEntity.status(500).body("Error occurred during registration.");
        }
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsersDTO usersDto) {
        log.info("Login attempt for email: {}", usersDto.getEmail());

        Optional<Users> userOptional = userRepository.findByEmail(usersDto.getEmail());

        if (userOptional.isEmpty()) {
            log.error("Login failed: Email not found - {}", usersDto.getEmail());
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        Users user = userOptional.get();

        // ✅ Fix: Proper password comparison
        if (!passwordEncoder.matches(usersDto.getPassword(), user.getPassword())) {
            log.error("Login failed: Incorrect password for - {}", usersDto.getEmail());
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        // ✅ Generate JWT Token
        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Login successful for user: {}", usersDto.getEmail());

        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }

    @GetMapping("/user-profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.replace("Bearer ", "")); // ✅ Extract email from JWT
            Optional<Users> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            Users user = userOptional.get();
            UsersDTO userDto = new UsersDTO(user.getName(), user.getLastname(), user.getEmail(),user.getBirthday(), user.getCountry(), user.getNationality(), user.getPhoneNumber(), user.getOccupation());

            return ResponseEntity.ok(userDto);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Token expired. Please log in again.");
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Invalid token.");
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody UsersDTO updatedUser) {
        try {
            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            Optional<Users> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found!");
            }

            Users user = userOptional.get();
            user.setName(updatedUser.getName());
            user.setLastname(updatedUser.getLastname());
            user.setBirthday(updatedUser.getBirthday());
            user.setCountry(updatedUser.getCountry());
            user.setNationality(updatedUser.getNationality());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setOccupation(updatedUser.getOccupation());

            userRepository.save(user);
            return ResponseEntity.ok().body("User profile updated successfully!");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Token expired. Please log in again.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user profile.");
        }
    }

}
