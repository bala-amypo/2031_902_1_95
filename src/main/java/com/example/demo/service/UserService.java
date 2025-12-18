package com.example.finance.service;

import com.example.finance.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();
}
