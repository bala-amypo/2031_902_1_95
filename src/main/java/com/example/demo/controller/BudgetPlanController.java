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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BudgetPlan body) {

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

    //GET ALL
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(planService.getAllByUser(getUser()));
    }

    // GET BY ID 
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getById(id));
    }

    //UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody BudgetPlan body
    ) {
        BudgetPlan updated = planService.updatePlan(
                id,
                body.getIncomeTarget(),
                body.getExpenseLimit()
        );

        return ResponseEntity.ok(updated);
    }

    // --- CRUD OPERATION ADDED: DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.ok("Budget plan deleted");
    }
}
