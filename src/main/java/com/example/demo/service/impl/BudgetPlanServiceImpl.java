package com.example.demo.service.impl;


import org.springframework.stereotype.Service;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.repository.UserRepository;

@Service
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private final BudgetPlanRepository budgetRepo;
    private final UserRepository userRepo;

    public BudgetPlanServiceImpl(BudgetPlanRepository budgetRepo,
                                 UserRepository userRepo) {
        this.budgetRepo = budgetRepo;
        this.userRepo = userRepo;
    }

    @Override
    public BudgetPlan createBudgetPlan(Long userId, BudgetPlan plan) {
        if (plan.getMonth() < 1 || plan.getMonth() > 12) {
            throw new BadRequestException("Invalid month");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        plan.setUser(user);
        return budgetRepo.save(plan);
    }

    @Override
    public BudgetPlan getBudgetPlan(Long userId, Integer month, Integer year) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return budgetRepo.findByUserAndMonthAndYear(user, month, year)
                .orElseThrow(() -> new BadRequestException("Budget plan not found"));
    }
}
