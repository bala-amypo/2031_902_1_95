package com.example.demo.service;

import com.example.demo.model.BudgetSummary;
import com.example.demo.model.User;

import java.util.List;

public interface BudgetSummaryService {

    BudgetSummary generateMonthlySummary(User user, Integer month, Integer year);

    BudgetSummary getSummary(Long id);

    // NEW CRUD METHODS
    List<BudgetSummary> getAll();

    BudgetSummary update(Long id, BudgetSummary newData);

    void delete(Long id);
}
