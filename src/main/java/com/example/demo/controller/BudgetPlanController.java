package com.example.demo.controller;

import com.example.demo.model.BudgetPlan;
import com.example.demo.model.User;
import com.example.demo.service.BudgetPlanService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget-plan")
public class BudgetPlanController {

    @Autowired
    private BudgetPlanService planService;

    @Autowired
    private UserService userService;

    private User getUser() {
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody BudgetPlan body) {

        User user = getUser();

        BudgetPlan plan = planService.createPlan(
                user,
                body.getMonth(),
                body.getYear(),
                body.getIncomeTarget(),
                body.getExpenseLimit()
        );

        return ResponseEntity.ok(plan);
    }

    @GetMapping
    public ResponseEntity<?> getPlan(
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {
        return ResponseEntity.ok(planService.getPlan(getUser(), month, year));
    }
}
