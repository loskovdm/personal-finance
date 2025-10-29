package com.example.personalfinance.domain.usecase.transaction;

import com.example.personalfinance.domain.exception.NotFoundException;
import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.repository.TransactionRepository;

public class GetTransactionUseCase {
    private final TransactionRepository repository;

    public GetTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction execute(int id) {
        validate(id);
        return repository.getTransaction(id);
    }

    private void validate(int id) {
        if (id <= 0) {
            throw new ValidationException("The transaction ID must be greater then 0");
        }
    }
}
