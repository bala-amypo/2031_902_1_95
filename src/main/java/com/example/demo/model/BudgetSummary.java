package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget_summary")
public class BudgetSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private BudgetPlan budgetPlan;

    private Double totalIncome;

    private Double totalExpense;

    private String status; 

    private LocalDateTime generatedAt;

    public BudgetSummary() {}

    public BudgetSummary(BudgetPlan plan, Double totalIncome, Double totalExpense) {
        this.budgetPlan = plan;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;

        this.status = totalExpense > plan.getExpenseLimit()
                ? "OVER_LIMIT"
                : "UNDER_LIMIT";

        this.generatedAt = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public BudgetPlan getBudgetPlan() { return budgetPlan; }
    public Double getTotalIncome() { return totalIncome; }
    public Double getTotalExpense() { return totalExpense; }
    public String getStatus() { return status; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}
