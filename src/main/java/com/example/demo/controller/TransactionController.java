package com.example.demo.controller;

import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService txService;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {

        User user = getCurrentUser();

        Long categoryId = Long.valueOf(body.get("categoryId").toString());
        Double amount = Double.valueOf(body.get("amount").toString());
        String description = (String) body.get("description");
        LocalDate date = LocalDate.parse(body.get("transactionDate").toString());

        TransactionLog tx = txService.createTransaction(user, categoryId, amount, description, date);

        return ResponseEntity.ok(tx);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(txService.getAllByUser(getCurrentUser()));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getByDate(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return ResponseEntity.ok(
                txService.getByDateRange(
                        getCurrentUser(),
                        LocalDate.parse(start),
                        LocalDate.parse(end)
                )
        );
    }
}
