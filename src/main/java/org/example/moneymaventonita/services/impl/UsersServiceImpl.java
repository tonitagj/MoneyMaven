package org.example.moneymaventonita.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.moneymaventonita.persistance.entities.Users;
import org.example.moneymaventonita.persistance.repositories.UserRepository;
import org.example.moneymaventonita.services.UsersService;
import org.example.moneymaventonita.services.dtos.UsersDTO;
import org.example.moneymaventonita.services.exceptions.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean saveNewUser(UsersDTO usersDto) {
        log.info("Attempting to register user with email: {}", usersDto.getEmail());

        // ✅ Prevent duplicate email registrations
        if (userRepository.findByEmail(usersDto.getEmail()).isPresent()) {
            log.warn("Duplicate email detected: {}", usersDto.getEmail());
            throw new RuntimeException("Email already exists! Please use a different email.");
        }

        // ✅ Save user with hashed password
        Users user = new Users();
        user.setName(usersDto.getName());
        user.setLastname(usersDto.getLastname());
        user.setEmail(usersDto.getEmail());
        user.setPassword(passwordEncoder.encode(usersDto.getPassword())); // ✅ Hash password
        user.setBirthday(usersDto.getBirthday());
        user.setCountry(usersDto.getCountry());
        user.setNationality(usersDto.getNationality());
        user.setPhoneNumber(usersDto.getPhoneNumber());
        user.setOccupation(usersDto.getOccupation());

        userRepository.save(user);
        log.info("User successfully registered: {}", usersDto.getEmail());
        return true;
    }
}