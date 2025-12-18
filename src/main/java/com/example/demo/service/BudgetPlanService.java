package com.example.demo.service;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;

public interface BudgetPlanService {

    BudgetPlan createPlan(
            User user,
            Integer month,
            Integer year,
            Double incomeTarget,
            Double expenseLimit
    );

    BudgetPlan getPlan(User user, Integer month, Integer year);
}
