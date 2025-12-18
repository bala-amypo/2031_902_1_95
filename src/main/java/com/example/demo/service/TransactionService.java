package com.example.demo.service;

import com.example.demo.model.TransactionLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    TransactionLog saveTransaction(TransactionLog transaction);

    Optional<TransactionLog> getTransactionById(Long id);

    List<TransactionLog> getTransactionsByUser(Long userId);

    List<TransactionLog> getTransactionsByUserAndDateRange(Long userId, LocalDate start, LocalDate end);
}
