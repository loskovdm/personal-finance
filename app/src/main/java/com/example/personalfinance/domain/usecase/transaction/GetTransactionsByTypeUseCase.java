package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.mapper.TransactionMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GetTransactionsByTypeUseCase {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public GetTransactionsByTypeUseCase(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> execute(TransactionType type) {
        List<TransactionWithCategoryId> transactionsWithCategoryId = transactionRepository.getTransactionsWithCategoryIdByType(type, 10);
        return transactionsWithCategoryId.stream()
                .map(transactionWithCategoryId -> {
                    Category category = categoryRepository.getCategory(transactionWithCategoryId.getId());
                    return TransactionMapper.toTransaction(transactionWithCategoryId, category);
                })
                .collect(Collectors.toList());
    }
}
