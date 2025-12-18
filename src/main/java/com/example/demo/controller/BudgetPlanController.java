package com.example.demo.controller;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BudgetPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/budget-plans")
public class BudgetPlanController {

    private final BudgetPlanService planService;
    private final UserRepository userRepository;

    public BudgetPlanController(BudgetPlanService planService,
                                UserRepository userRepository) {
        this.planService = planService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public BudgetPlan create(@RequestParam Long userId,
                             @RequestParam Integer month,
                             @RequestParam Integer year,
                             @RequestParam Double incomeTarget,
                             @RequestParam Double expenseLimit) {

        User user = userRepository.findById(userId).orElse(null);

        BudgetPlan plan = new BudgetPlan();
        plan.setUser(user);
        plan.setMonth(month);
        plan.setYear(year);
        plan.setIncomeTarget(incomeTarget);
        plan.setExpenseLimit(expenseLimit);

        return planService.saveBudgetPlan(plan);
    }

    @GetMapping("/{id}")
    public Optional<BudgetPlan> getById(@PathVariable Long id) {
        return planService.getBudgetPlanById(id);
    }

    @GetMapping
    public List<BudgetPlan> getAll() {
        return planService.getAllBudgetPlans();
    }
}
