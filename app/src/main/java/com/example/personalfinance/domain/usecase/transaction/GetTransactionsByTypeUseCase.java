package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.List;

public class GetTransactionsByTypeUseCase {
    private final TransactionRepository repository;

    public GetTransactionsByTypeUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> execute(TransactionType type) {
        return repository.getTransactionsByType(type, 10);
    }
}
