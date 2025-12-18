package com.example.demo.service;

import com.example.demo.model.Category;
import java.util.List;

public interface CategoryService {

    Category createCategory(String name, String type);

    List<Category> getAll();

    Category getById(Long id);
}
