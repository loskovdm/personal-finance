package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetBudgetFotDateUseCase {
    private final GetCurrentBudgetUseCase getCurrentBudgetUseCase;
    private final TransactionRepository transactionRepository;

    public GetBudgetFotDateUseCase(BudgetRepository budgetRepository, TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
    public GetBudgetFotDateUseCase(GetCurrentBudgetUseCase getCurrentBudgetUseCase,
                                   TransactionRepository transactionRepository) {
        this.getCurrentBudgetUseCase = getCurrentBudgetUseCase;
        this.transactionRepository = transactionRepository;
    }

    public int execute(Date date) {
        Date filterDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            filterDate = calendar.getTime();
        }
        List<TransactionWithCategoryId> transactionsWithCategoryId = transactionRepository.getTransactionsWithCategoryIdForPeriod(filterDate, null);

        int totalAmount = 0;
        for (TransactionWithCategoryId transactionWithCategoryId : transactionsWithCategoryId) {
            if (transactionWithCategoryId.getType() == TransactionType.INCOME) {
                totalAmount += transactionWithCategoryId.getAmount();
            } else {
                totalAmount -= transactionWithCategoryId.getAmount();
            }
        }

        int currentBudget = getCurrentBudgetUseCase.execute();
        return currentBudget - totalAmount;
    }
}
