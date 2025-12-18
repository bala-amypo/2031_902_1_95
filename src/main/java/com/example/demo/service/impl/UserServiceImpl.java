package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(String name, String email, String password) {

        if (userRepo.findByEmail(email) != null) {
            throw new ConflictException("Email already exists");
        }

        User user = new User(
                name,
                email,
                passwordEncoder.encode(password),
                "USER"
        );

        return userRepo.save(user);
    }

    @Override
    public User login(String email, String rawPassword) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        return user;
    }

    @Override
    public User getByEmail(String email) {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
    }
}
