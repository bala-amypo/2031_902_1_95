package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ConflictException;

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

        BudgetPlan plan = planRepo.findByUserAndMonthAndYear(user, month, year);

        if (plan == null) {
            throw new ResourceNotFoundException("Budget plan not found");
        }

        if (summaryRepo.findByBudgetPlan(plan) != null) {
            throw new ConflictException("Summary already generated");
        }

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

    // ---------------------------
    // NEW CRUD METHODS
    // ---------------------------

    @Override
    public List<BudgetSummary> getAll() {
        return summaryRepo.findAll();
    }

    @Override
    public BudgetSummary update(Long id, BudgetSummary newData) {

        BudgetSummary existing = getSummary(id);

        // Update only allowed fields
        existing.setTotalIncome(newData.getTotalIncome());
        existing.setTotalExpense(newData.getTotalExpense());
        existing.setStatus(newData.getStatus());

        return summaryRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        BudgetSummary existing = getSummary(id);
        summaryRepo.delete(existing);
    }
}
