package com.example.personalfinance.domain.usecase.category;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

public class DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DeleteCategoryUseCase(CategoryRepository categoryRepository,
                                 TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(int id) {
        validate(id);
        categoryRepository.deleteCategory(id);
    }

    private void validate(int id) {
        if (id <= 0 ) {
            throw new ValidationException("The category ID must be greater then 0");
        }

        if (transactionRepository.existsTransactionByCategory(id)) {
            throw new ValidationException("There are transactions with this category");
        }
    }
}
