package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;

import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.service.BudgetPlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetPlanServiceImpl implements BudgetPlanService {

    @Autowired
    private BudgetPlanRepository planRepo;

    @Override
    public BudgetPlan createPlan(
            User user,
            Integer month,
            Integer year,
            Double incomeTarget,
            Double expenseLimit
    ) {

        if (month < 1 || month > 12) {
            throw new BadRequestException("Invalid month");
        }

        if (incomeTarget < 0 || expenseLimit < 0) {
            throw new BadRequestException("Targets must be >= 0");
        }

        BudgetPlan existing = planRepo.findByUserAndMonthAndYear(user, month, year);

        if (existing != null) {
            throw new ConflictException("Budget plan already exists for this month");
        }

        BudgetPlan plan = new BudgetPlan(user, month, year, incomeTarget, expenseLimit);

        return planRepo.save(plan);
    }

    @Override
    public BudgetPlan getPlan(User user, Integer month, Integer year) {

        BudgetPlan plan = planRepo.findByUserAndMonthAndYear(user, month, year);

        if (plan == null) {
            throw new ResourceNotFoundException("Budget plan not found");
        }

        return plan;
    }
}
