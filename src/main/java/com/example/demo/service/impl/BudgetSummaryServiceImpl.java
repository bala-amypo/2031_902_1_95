package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.BudgetPlan;
import com.example.demo.model.BudgetSummary;
import com.example.demo.model.TransactionLog;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.repository.BudgetSummaryRepository;
import com.example.demo.repository.TransactionLogRepository;
import com.example.demo.service.BudgetSummaryService;

@Service
public class BudgetSummaryServiceImpl implements BudgetSummaryService {

    private final BudgetSummaryRepository summaryRepo;
    private final BudgetPlanRepository planRepo;
    private final TransactionLogRepository logRepo;

    public BudgetSummaryServiceImpl(BudgetSummaryRepository summaryRepo,
                                    BudgetPlanRepository planRepo) {
        this.summaryRepo = summaryRepo;
        this.planRepo = planRepo;
        this.logRepo = null;
    }

    public BudgetSummaryServiceImpl(BudgetSummaryRepository summaryRepo,
                                    BudgetPlanRepository planRepo,
                                    TransactionLogRepository logRepo) {
        this.summaryRepo = summaryRepo;
        this.planRepo = planRepo;
        this.logRepo = logRepo;
    }

    @Override
    public BudgetSummary generateSummary(Long budgetPlanId) {
        BudgetPlan plan = planRepo.findById(budgetPlanId)
                .orElseThrow(() -> new BadRequestException("Plan not found"));

        double income = 0;
        double expense = 0;

        if (logRepo != null) {
            List<TransactionLog> logs = logRepo.findByUser(plan.getUser());
            for (TransactionLog log : logs) {
                if ("INCOME".equalsIgnoreCase(log.getCategory().getType())) {
                    income += log.getAmount();
                } else {
                    expense += log.getAmount();
                }
            }
        }

        String status = expense <= plan.getExpenseLimit()
                ? "UNDER_LIMIT"
                : "OVER_LIMIT";

        BudgetSummary summary = new BudgetSummary(
                null,
                plan,
                income,
                expense,
                status,
                LocalDateTime.now()
        );

        return summaryRepo.save(summary);
    }

    @Override
    public BudgetSummary getSummary(Long budgetPlanId) {
        BudgetPlan plan = planRepo.findById(budgetPlanId)
                .orElseThrow(() -> new BadRequestException("Plan not found"));

        return summaryRepo.findByBudgetPlan(plan)
                .orElseThrow(() -> new BadRequestException("Summary not found"));
    }
}
