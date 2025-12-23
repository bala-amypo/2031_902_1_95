package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Long> {
    Optional<BudgetPlan> findByUserAndMonthAndYear(
            User user,
            Integer month,
            Integer year
    );
}
