package com.example.demo.service.impl;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.BudgetSummary;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.repository.BudgetSummaryRepository;
import com.example.demo.service.BudgetSummaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BudgetSummaryServiceImpl implements BudgetSummaryService {

    private final BudgetSummaryRepository summaryRepository;
    private final BudgetPlanRepository planRepository;

    public BudgetSummaryServiceImpl(BudgetSummaryRepository summaryRepository,
                                    BudgetPlanRepository planRepository) {
        this.summaryRepository = summaryRepository;
        this.planRepository = planRepository;
    }

    @Override
    public BudgetSummary saveBudgetSummary(BudgetSummary summary) {
        return summaryRepository.save(summary);
    }

    @Override
    public Optional<BudgetSummary> getBudgetSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    @Override
    public Optional<BudgetSummary> getBudgetSummaryByPlanId(Long budgetPlanId) {
        Optional<BudgetPlan> plan = planRepository.findById(budgetPlanId);
        return plan.isEmpty()
                ? Optional.empty()
                : summaryRepository.findByBudgetPlan(plan.get());
    }

    // Optional utility later:
    public BudgetSummary autoGenerate(BudgetPlan plan, double income, double expense) {
        BudgetSummary summary = new BudgetSummary();
        summary.setBudgetPlan(plan);
        summary.setTotalIncome(income);
        summary.setTotalExpense(expense);
        summary.setGeneratedAt(LocalDateTime.now());
        summary.setStatus(expense > plan.getExpenseLimit() ? "OVER_LIMIT" : "UNDER_LIMIT");
        return summaryRepository.save(summary);
    }
}
