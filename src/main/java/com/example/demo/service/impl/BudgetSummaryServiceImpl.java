package com.example.demo.service.impl;

import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.*;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.repository.BudgetSummaryRepository;
import com.example.demo.repository.TransactionLogRepository;

import com.example.demo.service.BudgetSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetSummaryServiceImpl implements BudgetSummaryService {

    @Autowired
    private BudgetPlanRepository planRepo;

    @Autowired
    private BudgetSummaryRepository summaryRepo;

    @Autowired
    private TransactionLogRepository transactionRepo;

    @Override
    public BudgetSummary generateMonthlySummary(User user, Integer month, Integer year) {

        // Get BudgetPlan
        BudgetPlan plan = planRepo.findByUserAndMonthAndYear(user, month, year);

        if (plan == null) {
            throw new ResourceNotFoundException("Budget plan not found");
        }

        // Enforce 1 summary per plan
        if (summaryRepo.findByBudgetPlan(plan) != null) {
            throw new ConflictException("Summary already generated");
        }

        // Fetch all transactions for the month
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<TransactionLog> txList =
                transactionRepo.findByUserAndTransactionDateBetween(user, start, end);

        double totalIncome = 0;
        double totalExpense = 0;

        for (TransactionLog tx : txList) {
            if (tx.getCategory().getType().equals("INCOME")) {
                totalIncome += tx.getAmount();
            } else {
                totalExpense += tx.getAmount();
            }
        }

        // Create summary
        BudgetSummary summary = new BudgetSummary(
                plan,
                totalIncome,
                totalExpense
        );

        return summaryRepo.save(summary);
    }

    @Override
    public BudgetSummary getSummary(Long id) {
        return summaryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Summary not found"));
    }
}
