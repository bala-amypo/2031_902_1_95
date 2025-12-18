package com.example.demo.controller;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.BudgetSummary;
import com.example.demo.repository.BudgetPlanRepository;
import com.example.demo.service.BudgetSummaryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/budget-summary")
public class BudgetSummaryController {

    private final BudgetSummaryService summaryService;
    private final BudgetPlanRepository planRepository;

    public BudgetSummaryController(BudgetSummaryService summaryService,
                                   BudgetPlanRepository planRepository) {
        this.summaryService = summaryService;
        this.planRepository = planRepository;
    }

    @PostMapping("/generate/{planId}")
    public BudgetSummary generate(@PathVariable Long planId) {

        Optional<BudgetPlan> planOpt = planRepository.findById(planId);

        if (planOpt.isEmpty()) {
            return null;
        }

        BudgetPlan plan = planOpt.get();

        BudgetSummary summary = new BudgetSummary();
        summary.setBudgetPlan(plan);
        summary.setGeneratedAt(LocalDateTime.now());
        summary.setStatus("UNDER_LIMIT");
        summary.setTotalIncome(0.0);
        summary.setTotalExpense(0.0);

        return summaryService.saveBudgetSummary(summary);
    }

    @GetMapping("/{planId}")
    public Optional<BudgetSummary> get(@PathVariable Long planId) {
        return summaryService.getBudgetSummaryByPlanId(planId);
    }
}
