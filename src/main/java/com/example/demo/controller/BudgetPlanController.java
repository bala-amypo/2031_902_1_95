package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.BudgetPlan;
import com.example.demo.service.BudgetPlanService;

@RestController
@RequestMapping("/budget-plans")
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;

    public BudgetPlanController(BudgetPlanService budgetPlanService) {
        this.budgetPlanService = budgetPlanService;
    }

    @PostMapping("/{userId}")
    public BudgetPlan createPlan(
            @PathVariable Long userId,
            @RequestBody BudgetPlan plan) {
        return budgetPlanService.createBudgetPlan(userId, plan);
    }

    @GetMapping("/{userId}")
    public BudgetPlan getPlan(
            @PathVariable Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return budgetPlanService.getBudgetPlan(userId, month, year);
    }
}
