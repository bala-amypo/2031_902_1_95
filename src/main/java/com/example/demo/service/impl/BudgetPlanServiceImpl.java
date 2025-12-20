@Override
public BudgetPlan getById(Long id) {
    return planRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Budget plan not found"));
}

@Override
public List<BudgetPlan> getAllByUser(User user) {
    return planRepo.findAll()
            .stream()
            .filter(plan -> plan.getUser().getId().equals(user.getId()))
            .toList();
}

@Override
public BudgetPlan updatePlan(Long id, Double incomeTarget, Double expenseLimit) {

    BudgetPlan plan = getById(id);

    if (incomeTarget < 0 || expenseLimit < 0) {
        throw new BadRequestException("Targets must be >= 0");
    }

    plan.setIncomeTarget(incomeTarget);
    plan.setExpenseLimit(expenseLimit);

    return planRepo.save(plan);
}

@Override
public void deletePlan(Long id) {
    BudgetPlan plan = getById(id);
    planRepo.delete(plan);
}
