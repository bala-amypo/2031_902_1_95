package com.example.demo.service;

import com.example.demo.model.Category;
import java.util.List;

public interface CategoryService {

    Category createCategory(String name, String type);

    List<Category> getAll();

    Category getById(Long id);
}


public interface CategoryService {

    Category createCategory(String name, String type);

    List<Category> getAll();

    Category getById(Long id);

    Category updateCategory(Long id, String name, String type);

    void deleteCategory(Long id);
}
