package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.repository.BudgetRepository;

public class SetBudgetUseCase {
    private final BudgetRepository repository;

    public SetBudgetUseCase(BudgetRepository repository) {
        this.repository = repository;
    }

    public void execute(int amount) {
        validate(amount);
        repository.setBudget(amount);
    }

    private void validate(int amount) {
        if (amount < 0) {
            throw new ValidationException("The budget must be greater than zero");
        }
    }
}
