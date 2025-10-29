package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.BudgetRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

public class DeleteTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final GetTransactionUseCase getTransactionUseCase;

    public DeleteTransactionUseCase(TransactionRepository transactionRepository,
                                    BudgetRepository budgetRepository,
                                    GetTransactionUseCase getTransactionUseCase) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.getTransactionUseCase = getTransactionUseCase;
    }

    public void execute(int id) {
        validate(id);

        Transaction transaction = getTransactionUseCase.execute(id);
        TransactionType typeToUpdate = transaction.getType() == TransactionType.INCOME ? TransactionType.EXPENSE : TransactionType.INCOME;

        transactionRepository.deleteTransaction(id);
        budgetRepository.updateBudget(typeToUpdate, transaction.getAmount());
    }

    private void validate(int id) {
        if (id <= 0) {
            throw new ValidationException("The transaction ID must be greater then 0");
        }
    }
}
