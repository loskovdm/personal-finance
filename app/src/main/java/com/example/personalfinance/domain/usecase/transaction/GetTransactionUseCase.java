package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.mapper.TransactionMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.TransactionRepository;
import com.example.personalfinance.domain.usecase.category.GetCategoryUseCase;

import javax.inject.Inject;

public class GetTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final GetCategoryUseCase getCategoryUseCase;

    @Inject
    public GetTransactionUseCase(TransactionRepository transactionRepository, GetCategoryUseCase getCategoryUseCase) {
        this.transactionRepository = transactionRepository;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    public Transaction execute(int id) {
        validate(id);
        TransactionWithCategoryId transactionWithCategoryId = transactionRepository.getTransactionWithCategoryId(id);
        Category category = getCategoryUseCase.execute(transactionWithCategoryId.getCategoryId());
        return TransactionMapper.toTransaction(transactionWithCategoryId, category);
    }

    private void validate(int id) {
        if (id <= 0) {
            throw new ValidationException("The transaction ID must be greater then 0");
        }
    }
}
