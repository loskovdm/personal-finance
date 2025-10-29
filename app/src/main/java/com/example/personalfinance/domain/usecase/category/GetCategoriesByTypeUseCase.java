package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.CategoryRepository;

import java.util.List;

public class GetCategoriesByTypeUseCase {
    private final CategoryRepository repository;

    public GetCategoriesByTypeUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> execute(TransactionType type) {
        return repository.getCategoriesByType(type);
    }
}
