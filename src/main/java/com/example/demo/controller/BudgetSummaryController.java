package com.example.demo.controller;

import com.example.demo.model.BudgetSummary;
import com.example.demo.model.User;

import com.example.demo.service.BudgetSummaryService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget-summary")
public class BudgetSummaryController {

    @Autowired
    private BudgetSummaryService summaryService;

    @Autowired
    private UserService userService;

    private User getUser() {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername(); 
        } else {
            email = principal.toString();
        }

        return userService.getByEmail(email);
    }


    // CREATE SUMMARY
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

    @GetMapping
    public ResponseEntity<List<BudgetSummary>> getAllSummaries() {
        return ResponseEntity.ok(summaryService.getAll());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSummary(
            @PathVariable Long id,
            @RequestBody BudgetSummary body
    ) {
        return ResponseEntity.ok(summaryService.update(id, body));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSummary(@PathVariable Long id) {
        summaryService.delete(id);
        return ResponseEntity.ok("Budget summary deleted successfully");
    }
}
