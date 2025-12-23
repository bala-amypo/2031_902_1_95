package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.BudgetSummary;
import com.example.demo.model.BudgetPlan;

public interface BudgetSummaryRepository extends JpaRepository<BudgetSummary, Long> {
    Optional<BudgetSummary> findByBudgetPlan(BudgetPlan plan);
}
