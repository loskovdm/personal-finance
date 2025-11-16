package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.repository.BudgetRepository;

import javax.inject.Inject;

public class GetCurrentBudgetUseCase {
    private final BudgetRepository repository;

    @Inject
    public GetCurrentBudgetUseCase(BudgetRepository repository) {
        this.repository = repository;
    }

    public int execute() {
        return repository.getBudget();
    }
}
