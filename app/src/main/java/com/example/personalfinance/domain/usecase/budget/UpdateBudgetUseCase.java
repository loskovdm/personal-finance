package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.BudgetRepository;

public class UpdateBudgetUseCase {
    private final GetCurrentBudgetUseCase getCurrentBudgetUseCase;
    private final SetBudgetUseCase setBudgetUseCase;

    public UpdateBudgetUseCase(BudgetRepository repository) {
        this.repository = repository;
    public UpdateBudgetUseCase(GetCurrentBudgetUseCase getCurrentBudgetUseCase,
                               SetBudgetUseCase setBudgetUseCase) {
        this.getCurrentBudgetUseCase = getCurrentBudgetUseCase;
        this.setBudgetUseCase = setBudgetUseCase;
    }

    public void execute(TransactionType type, int amount) {
        int currentBudget = getCurrentBudgetUseCase.execute();
        int newBudget = type == TransactionType.INCOME
                ? currentBudget + amount
                : currentBudget - amount;
        setBudgetUseCase.execute(newBudget);
    }
}
