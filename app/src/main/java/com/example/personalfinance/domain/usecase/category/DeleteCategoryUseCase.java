package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.repository.CategoryRepository;

public class DeleteCategoryUseCase {
    private final CategoryRepository repository;

    public DeleteCategoryUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public void execute(int id) {
        validate(id);
        repository.deleteCategory(id);
    }

    private void validate(int id) {
        if (id <= 0 ) {
            throw new ValidationException("The category ID must be greater then 0");
        }
    }
}
