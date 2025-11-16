package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.repository.CategoryRepository;

import javax.inject.Inject;

public class GetCategoryUseCase {
    private final CategoryRepository repository;

    @Inject
    public GetCategoryUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category execute(int id) {
        validate(id);
        return repository.getCategory(id);
    }

    private void validate(int id) {
        if (id <= 0) {
            throw new ValidationException("The category must be greater then 0");
        }
    }
}
