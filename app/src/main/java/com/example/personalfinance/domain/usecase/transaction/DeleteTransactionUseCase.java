package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.TransactionRepository;
import com.example.personalfinance.domain.usecase.budget.UpdateBudgetUseCase;

import javax.inject.Inject;

public class DeleteTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final UpdateBudgetUseCase updateBudgetUseCase;

    @Inject
    public DeleteTransactionUseCase(TransactionRepository transactionRepository,
                                    UpdateBudgetUseCase updateBudgetUseCase) {
        this.transactionRepository = transactionRepository;
        this.updateBudgetUseCase = updateBudgetUseCase;
    }

    public void execute(Transaction transaction) {
        validate(transaction);

        TransactionType typeToUpdate = transaction.getType() == TransactionType.INCOME ? TransactionType.EXPENSE : TransactionType.INCOME;

        transactionRepository.deleteTransaction(transaction);
        updateBudgetUseCase.execute(typeToUpdate, transaction.getAmount());
    }

    private void validate(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must not be null");
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
