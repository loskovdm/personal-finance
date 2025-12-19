package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

import javax.inject.Inject;

public class DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Inject
    public DeleteCategoryUseCase(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(Category category) {
        validate(category);
        if (transactionRepository.existsTransactionByCategory(category.getId())) {
            throw new ValidationException("Cannot delete category because it has associated transactions");
        }
        categoryRepository.deleteCategory(category);
    }

    private void validate(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        if (category.getType() == null) {
            throw new ValidationException("Category type must be specified");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }
    }
}
