package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.TransactionLog;
import com.example.demo.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}")
    public TransactionLog addTransaction(
            @PathVariable Long userId,
            @RequestBody TransactionLog log) {
        return transactionService.addTransaction(userId, log);
    }

    @GetMapping("/{userId}")
    public List<TransactionLog> getUserTransactions(@PathVariable Long userId) {
        return transactionService.getUserTransactions(userId);
    }
}
