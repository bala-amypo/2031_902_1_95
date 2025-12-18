package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public Category createCategory(String name, String type) {

        if (categoryRepo.existsByName(name)) {
            throw new ConflictException("Category already exists");
        }

        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            throw new BadRequestException("Invalid category type");
        }

        Category cat = new Category(name, type);

        return categoryRepo.save(cat);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getById(Long id) {

        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
