package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.CategoryReport;
import com.example.personalfinance.domain.model.CategoryValue;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetCategoryReportUseCase {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetCategoryReportUseCase(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategoryReport execute(TransactionType type,
                                  Date startDate,
                                  Date finishDate,
                                  List<Category> includedCategories) {
        int totalAmount = transactionRepository.getTransactionTotalAmountByType(
                type,
                startDate,
                finishDate,
                includedCategories);

        List<Category> categories = categoryRepository.getCategoriesByType(type);
        List<Integer> categoryIds = null;

        if (includedCategories != null) {
            categories = categories.stream()
                    .filter(c -> includedCategories.stream()
                            .anyMatch(ic -> ic.getId() == c.getId()))
                    .collect(Collectors.toList());
            categoryIds = categories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }

        Map<Integer, Integer> categoryIdValue = transactionRepository.getTransactionAmountsByCategories(
                categoryIds,
                startDate,
                finishDate
        );

        List<CategoryValue> categoryValues = categories.stream()
                .map(category -> {
                    Integer amount = categoryIdValue.get(category.getId());
                    return new CategoryValue(category, amount != null ? amount : 0);
                })
                .collect(Collectors.toList());

        return new CategoryReport(totalAmount, categoryValues);
    }
}
