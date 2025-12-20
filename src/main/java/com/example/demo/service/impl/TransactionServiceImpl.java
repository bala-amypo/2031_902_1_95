package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.Category;
import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TransactionLogRepository;

import com.example.demo.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionLogRepository transactionRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public TransactionLog createTransaction(
            User user,
            Long categoryId,
            Double amount,
            String description,
            LocalDate date
    ) {

        if (amount == null || amount <= 0) {
            throw new BadRequestException("Amount must be > 0");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new BadRequestException("Future date not allowed");
        }

        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        TransactionLog tx = new TransactionLog(
                user,
                cat,
                amount,
                description,
                date
        );

        return transactionRepo.save(tx);
    }

    @Override
    public List<TransactionLog> getAllByUser(User user) {
        return transactionRepo.findByUser(user);
    }

    @Override
    public List<TransactionLog> getByDateRange(User user, LocalDate start, LocalDate end) {

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        return transactionRepo.findByUserAndTransactionDateBetween(user, start, end);
    }
    // ------------------- CRUD OPERATION ADDED: GET BY ID -------------------
@Override
public TransactionLog getById(Long id) {
    return transactionRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
}

// ------------------- CRUD OPERATION ADDED: UPDATE -------------------
@Override
public TransactionLog updateTransaction(
        Long id,
        User user,
        Long categoryId,
        Double amount,
        String description,
        LocalDate date
) {

    TransactionLog existing = getById(id);

    Category cat = categoryRepo.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

    if (amount <= 0) throw new BadRequestException("Invalid amount");
    if (date.isAfter(LocalDate.now())) throw new BadRequestException("Future date not allowed");

    existing.setUser(user);
    existing.setCategory(cat);
    existing.setAmount(amount);
    existing.setDescription(description);
    existing.setTransactionDate(date);

    return transactionRepo.save(existing);
}

// ------------------- CRUD OPERATION ADDED: DELETE -------------------
@Override
public void deleteTransaction(Long id) {
    TransactionLog tx = getById(id);
    transactionRepo.delete(tx);
}

}
