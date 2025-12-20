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

    // --- CRUD OPERATION ADDED ---
    BudgetPlan getById(Long id);

    // --- CRUD OPERATION ADDED ---
    List<BudgetPlan> getAllByUser(User user);

    // --- CRUD OPERATION ADDED ---
    BudgetPlan updatePlan(
            Long id,
            Double incomeTarget,
            Double expenseLimit
    );

    // --- CRUD OPERATION ADDED ---
    void deletePlan(Long id);
}
