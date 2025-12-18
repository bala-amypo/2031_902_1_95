package com.example.demo.service.impl;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BudgetPlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private final BudgetPlanRepository planRepository;
    private final UserRepository userRepository;

    public BudgetPlanServiceImpl(BudgetPlanRepository planRepository,
                                 UserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BudgetPlan saveBudgetPlan(BudgetPlan plan) {
        return planRepository.save(plan);
    }

    @Override
    public Optional<BudgetPlan> getBudgetPlanById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public Optional<BudgetPlan> getBudgetPlanForUserMonth(Long userId, Integer month, Integer year) {
        Optional<User> user = userRepository.findById(userId);
        return user.isEmpty()
                ? Optional.empty()
                : planRepository.findByUserAndMonthAndYear(user.get(), month, year);
    }

    @Override
    public List<BudgetPlan> getAllBudgetPlans() {
        return planRepository.findAll();
    }
}
