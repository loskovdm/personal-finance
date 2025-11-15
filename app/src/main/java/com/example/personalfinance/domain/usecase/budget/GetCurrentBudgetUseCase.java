package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.repository.BudgetRepository;

public class GetCurrentBudgetUseCase {
    private final BudgetRepository repository;

    public GetCurrentBudgetUseCase(BudgetRepository repository) {
        this.repository = repository;
    }

    public int execute() {
        return repository.getBudget();
    }
}
