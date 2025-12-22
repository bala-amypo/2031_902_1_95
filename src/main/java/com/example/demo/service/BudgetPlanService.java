package com.example.demo.service;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;

import java.util.List;

public interface BudgetPlanService {

    BudgetPlan createPlan(
            User user,
            Integer month,
            Integer year,
            Double incomeTarget,
            Double expenseLimit
    );

    BudgetPlan getPlan(User user, Integer month, Integer year);
    BudgetPlan getById(Long id);
    List<BudgetPlan> getAllByUser(User user);
    BudgetPlan updatePlan(
            Long id,
            Double incomeTarget,
            Double expenseLimit
    );
    void deletePlan(Long id);
}
