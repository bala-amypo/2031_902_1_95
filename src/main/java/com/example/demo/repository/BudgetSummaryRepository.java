package com.example.demo.repository;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.BudgetSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetSummaryRepository
        extends JpaRepository<BudgetSummary, Long> {

    Optional<BudgetSummary> findByBudgetPlan(BudgetPlan plan);
}
