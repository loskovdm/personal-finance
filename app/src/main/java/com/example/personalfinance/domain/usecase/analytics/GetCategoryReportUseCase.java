package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.CategoryReport;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;

public class GetCategoryReportUseCase {
    private final TransactionRepository repository;

    public GetCategoryReportUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public CategoryReport execute(TransactionType type,
                                  Date startDate,
                                  Date finishDate,
                                  List<Category> includedCategories) {
        return repository.getTransactionCategoryReport(type, startDate, finishDate, includedCategories);
    }
}
