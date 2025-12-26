package com.example.demo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;
import com.example.demo.repository.TransactionLogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionLogRepository transactionRepo;
    private final UserRepository userRepo;

    public TransactionServiceImpl(TransactionLogRepository transactionRepo,
                                  UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    @Override
    public TransactionLog addTransaction(Long userId, TransactionLog log) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (log.getAmount() <= 0) {
            throw new BadRequestException("Invalid amount");
        }

        if (log.getTransactionDate() == null) {
            throw new BadRequestException("Transaction date required");
        }

        if (log.getTransactionDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Future date not allowed");
        }

        log.setUser(user);
        return transactionRepo.save(log);
    }

    @Override
    public List<TransactionLog> getUserTransactions(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return transactionRepo.findByUser(user);
    }
}
