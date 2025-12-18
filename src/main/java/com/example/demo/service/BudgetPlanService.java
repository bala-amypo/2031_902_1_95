package com.example.demo.service;

import com.example.demo.model.BudgetPlan;

import java.util.List;
import java.util.Optional;

public interface BudgetPlanService {

    BudgetPlan saveBudgetPlan(BudgetPlan plan);

    Optional<BudgetPlan> getBudgetPlanById(Long id);

    Optional<BudgetPlan> getBudgetPlanForUserMonth(Long userId, Integer month, Integer year);

    List<BudgetPlan> getAllBudgetPlans();
}
