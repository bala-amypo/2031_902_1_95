package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category created = categoryService.createCategory(category.getName(), category.getType());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

     // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(
            @PathVariable Long id,
            @RequestBody Category body
    ) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, body.getName(), body.getType())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
public ResponseEntity<?> delete(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Category deleted");
}
}




