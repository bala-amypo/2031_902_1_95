package com.example.demo.service;

import com.example.demo.model.BudgetSummary;

import java.util.Optional;

public interface BudgetSummaryService {

    BudgetSummary saveBudgetSummary(BudgetSummary summary);

    Optional<BudgetSummary> getBudgetSummaryById(Long id);

    Optional<BudgetSummary> getBudgetSummaryByPlanId(Long budgetPlanId);
}
