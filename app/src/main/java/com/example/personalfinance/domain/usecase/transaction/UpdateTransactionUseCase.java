package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.BudgetRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;
import com.example.personalfinance.domain.usecase.budget.UpdateBudgetUseCase;

public class UpdateTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final GetTransactionUseCase getTransactionUseCase;
    private final UpdateBudgetUseCase updateBudgetUseCase;

    public UpdateTransactionUseCase(TransactionRepository transactionRepository,
                                    BudgetRepository budgetRepository,
                                    GetTransactionUseCase getTransactionUseCase,
                                    UpdateBudgetUseCase updateBudgetUseCase) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.getTransactionUseCase = getTransactionUseCase;
        this.updateBudgetUseCase = updateBudgetUseCase;
    }

    public void execute(Transaction updatedTransaction) {
        validate(updatedTransaction);

        Transaction oldTransaction = getTransactionUseCase.execute(updatedTransaction.getId());
        int differenceAmount = updatedTransaction.getAmount() - oldTransaction.getAmount();
        TransactionType updatedType = differenceAmount >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE;
        int updatedAmount = Math.abs(differenceAmount);

        transactionRepository.updateTransaction(updatedTransaction);
        updateBudgetUseCase.execute(updatedType, updatedAmount);
    }

    private void validate(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction must be not null");
        }

        if (transaction.getAmount() <= 0) {
            throw new ValidationException("Transaction amount must be greater then 0");
        }

        if (transaction.getType() == null) {
            throw new ValidationException("Transaction type must be specified");
        }

        if (transaction.getCategory() == null) {
            throw new ValidationException("Transaction category must not be null");
        }

        if (transaction.getCategory().getType() != transaction.getType()) {
            throw new ValidationException("Transaction type must match category type");
        }
    }
}
