package com.example.demo.service;

import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    // ---------------- CREATE ----------------
    TransactionLog createTransaction(
            User user,
            Long categoryId,
            Double amount,
            String description,
            LocalDate date
    );

    // ---------------- READ (ALL BY USER) ----------------
    List<TransactionLog> getAllByUser(User user);

    // ---------------- READ (FILTER BY DATE) ----------------
    List<TransactionLog> getByDateRange(User user, LocalDate start, LocalDate end);


    // ------------------- CRUD OPERATION ADDED: READ ONE -------------------
    TransactionLog getById(Long id);

    // ------------------- CRUD OPERATION ADDED: UPDATE -------------------
    TransactionLog updateTransaction(
            Long id,
            User user,
            Long categoryId,
            Double amount,
            String description,
            LocalDate date
    );

    // ------------------- CRUD OPERATION ADDED: DELETE -------------------
    void deleteTransaction(Long id);
}
