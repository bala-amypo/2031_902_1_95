package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {

    User register(String name, String email, String password);

    User login(String email, String rawPassword);

    User getByEmail(String email);
}
