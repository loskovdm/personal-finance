package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.repository.BudgetRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;
import com.example.personalfinance.domain.usecase.budget.UpdateBudgetUseCase;

public class AddTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final UpdateBudgetUseCase updateBudgetUseCase;
    public AddTransactionUseCase(TransactionRepository transactionRepository,
                                 BudgetRepository budgetRepository,
                                 UpdateBudgetUseCase updateBudgetUseCase) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.updateBudgetUseCase = updateBudgetUseCase;
    }

    public void execute(Transaction transaction) {
        validate(transaction);
        transactionRepository.addTransaction(transaction);
        updateBudgetUseCase.execute(transaction.getType(), transaction.getAmount());
    }

    private void validate(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must not be null");
        }

        if (transaction.getId() != 0) {
            throw new ValidationException("The new transaction must have an ID of 0");
        }

        if (transaction.getAmount() <= 0) {
            throw new ValidationException("Amount must be greater then 0");
        }

        if (transaction.getType() == null) {
            throw new ValidationException("Transaction type must be specified");
        }

        if (transaction.getCategory() == null) {
            throw new ValidationException("Category must be provided");
        }

        if (transaction.getCategory().getType() != transaction.getType()) {
            throw new ValidationException("Transaction type must match category type");
        }
    }
}
