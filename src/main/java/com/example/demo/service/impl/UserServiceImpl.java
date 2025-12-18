package com.example.demo.service.impl;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public User registerUser(RegisterRequest req) {
        if (userRepo.findByEmail(req.getEmail()) != null)
            throw new ConflictException("Email already exists");

        User user = new User(req.getName(), req.getEmail(), req.getPassword());
        user.validate();

        user.setPassword(encoder.encode(req.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public User loginUser(LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail());
        if (user == null)
            throw new ResourceNotFoundException("User not found");

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new BadRequestException("Invalid password");

        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepo.findByEmail(email);

        if (user == null)
            throw new ResourceNotFoundException("User not found");

        return user;
    }
}
