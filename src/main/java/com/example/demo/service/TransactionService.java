package com.example.demo.service;

import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionLog createTransaction(
            User user,
            Long categoryId,
            Double amount,
            String description,
            LocalDate date
    );
    List<TransactionLog> getAllByUser(User user);

    List<TransactionLog> getByDateRange(User user, LocalDate start, LocalDate end);

    TransactionLog getById(Long id);

    TransactionLog updateTransaction(
            Long id,
            User user,
            Long categoryId,
            Double amount,
            String description,
            LocalDate date
    );
    void deleteTransaction(Long id);
}
