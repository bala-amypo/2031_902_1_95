package com.example.demo.service;

import com.example.demo.model.BudgetSummary;
import com.example.demo.model.User;

public interface BudgetSummaryService {

    BudgetSummary generateMonthlySummary(
            User user,
            Integer month,
            Integer year
    );

    BudgetSummary getSummary(Long id);
}
