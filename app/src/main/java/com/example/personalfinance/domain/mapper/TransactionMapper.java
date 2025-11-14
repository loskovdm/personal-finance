package com.example.personalfinance.domain.mapper;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;

public class TransactionMapper {
    public static Transaction toTransaction(TransactionWithCategoryId transactionWithCategoryId,
                                       Category category) {
        return new Transaction(
                transactionWithCategoryId.getId(),
                transactionWithCategoryId.getDate(),
                transactionWithCategoryId.getType(),
                category,
                transactionWithCategoryId.getAmount()
        );
    }

    public static TransactionWithCategoryId toTransactionWithCategoryId(Transaction transaction) {
        return new TransactionWithCategoryId(
                transaction.getId(),
                transaction.getDateTime(),
                transaction.getType(),
                transaction.getCategory().getId(),
                transaction.getAmount()
        );
    }
}
