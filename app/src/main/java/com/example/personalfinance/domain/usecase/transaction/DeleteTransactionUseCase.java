package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.BudgetRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

public class DeleteTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    public DeleteTransactionUseCase(TransactionRepository transactionRepository,
                                    BudgetRepository budgetRepository,
                                    GetTransactionUseCase getTransactionUseCase) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    public void execute(Transaction transaction) {
        validate(transaction);

        TransactionType typeToUpdate = transaction.getType() == TransactionType.INCOME ? TransactionType.EXPENSE : TransactionType.INCOME;

        transactionRepository.deleteTransaction(transaction);
        budgetRepository.updateBudget(typeToUpdate, transaction.getAmount());
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
