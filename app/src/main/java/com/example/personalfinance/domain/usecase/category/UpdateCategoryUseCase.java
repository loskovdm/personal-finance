package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.repository.CategoryRepository;

public class UpdateCategoryUseCase {
    private final CategoryRepository repository;

    public UpdateCategoryUseCase(CategoryRepository repository) {
        this.repository = repository;
    }

    public void execute(Category category) {
        validate(category);
        repository.updateCategory(category);
    }

    private void validate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        if (category.getId() <= 0) {
            throw new ValidationException("The category must be greater then 0");
        }

        if (category.getType() == null) {
            throw new ValidationException("Category type must be specified");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        if (repository.existsByNameAndType(category.getName(), category.getType())) {
            throw new ValidationException("Category with this name and type already exists");
        }
    }
}
