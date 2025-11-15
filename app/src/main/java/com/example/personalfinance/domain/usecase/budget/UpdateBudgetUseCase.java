package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.BudgetRepository;

public class UpdateBudgetUseCase {
    private final BudgetRepository repository;

    public UpdateBudgetUseCase(BudgetRepository repository) {
        this.repository = repository;
    }

    public void execute(TransactionType type, int amount) {
        int currentBudget = repository.getBudget();
        int newBudget = type == TransactionType.INCOME
                ? currentBudget + amount
                : currentBudget - amount;
        repository.setBudget(newBudget);
    }
}
