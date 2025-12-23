package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Category;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
}
