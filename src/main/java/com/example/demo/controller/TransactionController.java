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

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService txService;

    @Autowired
    private UserService userService;

    // Correct way to get logged-in user email
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByEmail(email);
    }

    // ------------------- CRUD OPERATION ADDED: CREATE -------------------
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionLog body) {

        User user = getCurrentUser();
        Long categoryId = body.getCategory().getId();

        TransactionLog tx = txService.createTransaction(
                user,
                categoryId,
                body.getAmount(),
                body.getDescription(),
                body.getTransactionDate()
        );

        return ResponseEntity.ok(tx);
    }

    // ------------------- CRUD OPERATION ADDED: READ ALL -------------------
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(txService.getAllByUser(getCurrentUser()));
    }

    // ------------------- CRUD OPERATION ADDED: READ ONE -------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(txService.getById(id));
    }

    // ------------------- CRUD OPERATION ADDED: UPDATE -------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody TransactionLog body
    ) {
        User user = getCurrentUser();

        TransactionLog updated = txService.updateTransaction(
                id,
                user,
                body.getCategory().getId(),
                body.getAmount(),
                body.getDescription(),
                body.getTransactionDate()
        );

        return ResponseEntity.ok(updated);
    }

    // ------------------- CRUD OPERATION ADDED: DELETE -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        txService.deleteTransaction(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    // ------------------- CRUD OPERATION ADDED: FILTER BY DATE -------------------
    @GetMapping("/filter")
    public ResponseEntity<?> getByDate(
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        return ResponseEntity.ok(
                txService.getByDateRange(getCurrentUser(), s, e)
        );
    }
}
