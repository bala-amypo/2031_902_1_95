package com.example.demo.service.impl;

import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;
import com.example.demo.repository.TransactionLogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionLogRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionLogRepository transactionRepository,
                                  UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TransactionLog saveTransaction(TransactionLog transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<TransactionLog> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<TransactionLog> getTransactionsByUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user == null ? List.of() : transactionRepository.findByUser(user);
    }

    @Override
    public List<TransactionLog> getTransactionsByUserAndDateRange(Long userId, LocalDate start, LocalDate end) {
        User user = userRepository.findById(userId).orElse(null);
        return user == null ? List.of()
                : transactionRepository.findByUserAndTransactionDateBetween(user, start, end);
    }
}
