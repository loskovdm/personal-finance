package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionTypeReport;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;

public class GetTransactionTypeReportUseCase {
    private final TransactionRepository repository;

    public GetTransactionTypeReportUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionTypeReport execute(Date startDate,
                                         Date finishDate,
                                         List<Category> includedCategories) {
        return repository.getTransactionTypeReport(startDate, finishDate, includedCategories);
    }
}
