package com.example.demo.controller;

import com.example.demo.model.BudgetSummary;
import com.example.demo.model.User;

import com.example.demo.service.BudgetSummaryService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget-summary")
public class BudgetSummaryController {

    @Autowired
    private BudgetSummaryService summaryService;

    @Autowired
    private UserService userService;

    private User getUser() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> generate(
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        BudgetSummary summary = summaryService.generateMonthlySummary(getUser(), month, year);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(summaryService.getSummary(id));
    }
}
