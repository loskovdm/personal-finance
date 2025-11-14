package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.mapper.TransactionMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

public class GetTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetTransactionUseCase(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction execute(int id) {
        validate(id);
        TransactionWithCategoryId transactionWithCategoryId = transactionRepository.getTransactionWithCategoryId(id);
        Category category = categoryRepository.getCategory(transactionWithCategoryId.getCategoryId());
        return TransactionMapper.toTransaction(transactionWithCategoryId, category);
    }

    private void validate(int id) {
        if (id <= 0) {
            throw new ValidationException("The transaction ID must be greater then 0");
        }
    }
}
