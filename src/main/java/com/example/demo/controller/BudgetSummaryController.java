package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.BudgetSummary;
import com.example.demo.service.BudgetSummaryService;

@RestController
@RequestMapping("/budget-summaries")
public class BudgetSummaryController {

    private final BudgetSummaryService summaryService;

    public BudgetSummaryController(BudgetSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping("/{planId}")
    public BudgetSummary generateSummary(@PathVariable Long planId) {
        return summaryService.generateSummary(planId);
    }

    @GetMapping("/{planId}")
    public BudgetSummary getSummary(@PathVariable Long planId) {
        return summaryService.getSummary(planId);
    }
}
