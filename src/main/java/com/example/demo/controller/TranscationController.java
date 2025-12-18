package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionController(TransactionService transactionService,
                                 UserRepository userRepository,
                                 CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public TransactionLog create(@RequestParam Long userId,
                                 @RequestParam Long categoryId,
                                 @RequestParam Double amount,
                                 @RequestParam(required = false) String description) {

        User user = userRepository.findById(userId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);

        TransactionLog tx = new TransactionLog();
        tx.setUser(user);
        tx.setCategory(category);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setTransactionDate(LocalDate.now());

        return transactionService.saveTransaction(tx);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionLog> getByUser(@PathVariable Long userId) {
        return transactionService.getTransactionsByUser(userId);
    }
}
