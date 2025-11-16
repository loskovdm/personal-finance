package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionTypeReport;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class GetTransactionTypeReportUseCase {
    private final TransactionRepository repository;

    @Inject
    public GetTransactionTypeReportUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionTypeReport execute(Date startDate,
                                         Date finishDate,
                                         List<Category> includedCategories) {
        if (startDate.after(finishDate)) {
            throw new ValidationException("The start date must be before the finish date");
        }

        int incomeAmount = repository.getTransactionTotalAmountByType(
                TransactionType.INCOME,
                startDate,
                finishDate,
                includedCategories);

        int expenseAmount = repository.getTransactionTotalAmountByType(
                TransactionType.EXPENSE,
                startDate,
                finishDate,
                includedCategories);

        return new TransactionTypeReport(incomeAmount, expenseAmount);
    }
}
