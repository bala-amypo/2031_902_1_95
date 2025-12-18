package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        Category created = categoryService.createCategory(category.getName(), category.getType());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Category> list = categoryService.getAll();
        return ResponseEntity.ok(list);
    }
}
