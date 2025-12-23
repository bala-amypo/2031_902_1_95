package com.example.demo.service;

import com.example.demo.model.BudgetSummary;

public interface BudgetSummaryService {
    BudgetSummary generateSummary(Long budgetPlanId);
    BudgetSummary getSummary(Long budgetPlanId);
}
