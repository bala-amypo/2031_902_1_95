package com.example.demo.model;

import com.example.demo.exception.BadRequestException;
import jakarta.persistence.*;

@Entity
@Table(
        name = "budget_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "month", "year"})
)
public class BudgetPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    private Integer month;        // 1–12
    private Integer year;
    private Double incomeTarget;
    private Double expenseLimit;

    public BudgetPlan() {}

    public BudgetPlan(User user, Integer month, Integer year,
                      Double incomeTarget, Double expenseLimit) {

        if (month < 1 || month > 12) throw new BadRequestException("Invalid month");
        if (incomeTarget < 0 || expenseLimit < 0)
            throw new BadRequestException("Limits must be >= 0");

        this.user = user;
        this.month = month;
        this.year = year;
        this.incomeTarget = incomeTarget;
        this.expenseLimit = expenseLimit;
    }

    // ---------- Getters ----------
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Integer getMonth() { return month; }
    public Integer getYear() { return year; }
    public Double getIncomeTarget() { return incomeTarget; }
    public Double getExpenseLimit() { return expenseLimit; }

    // ---------- Setters (REQUIRED for Swagger) ----------
    public void setUser(User user) { this.user = user; }

    public void setMonth(Integer month) { this.month = month; }

    public void setYear(Integer year) { this.year = year; }

    public void setIncomeTarget(Double incomeTarget) { this.incomeTarget = incomeTarget; }

    public void setExpenseLimit(Double expenseLimit) { this.expenseLimit = expenseLimit; }
}
