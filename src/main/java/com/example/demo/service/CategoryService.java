package com.example.demo.service;

import com.example.demo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category saveCategory(Category category);

    Optional<Category> getCategoryById(Long id);

    List<Category> getAllCategories();

    boolean existsByName(String name);
}
