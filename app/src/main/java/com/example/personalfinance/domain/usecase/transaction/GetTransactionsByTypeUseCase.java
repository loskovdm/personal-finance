package com.example.personalfinance.domain.usecase.transaction;

import android.util.Log;

import com.example.personalfinance.domain.mapper.TransactionMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.TransactionRepository;
import com.example.personalfinance.domain.usecase.category.GetCategoryUseCase;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GetTransactionsByTypeUseCase {
    private final TransactionRepository transactionRepository;
    private final GetCategoryUseCase getCategoryUseCase;

    @Inject
    public GetTransactionsByTypeUseCase(TransactionRepository transactionRepository, GetCategoryUseCase getCategoryUseCase) {
        this.transactionRepository = transactionRepository;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    public List<Transaction> execute(TransactionType type, int numberTransactions) {
        List<TransactionWithCategoryId> transactionsWithCategoryId = transactionRepository.getTransactionsWithCategoryIdByType(type, numberTransactions);
        return transactionsWithCategoryId.stream()
                .map(transactionWithCategoryId -> {
                    Category category = getCategoryUseCase.execute(transactionWithCategoryId.getCategoryId());
                    return TransactionMapper.toTransaction(transactionWithCategoryId, category);
                })
                .collect(Collectors.toList());
    }
}
