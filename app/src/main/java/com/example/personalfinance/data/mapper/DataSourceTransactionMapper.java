package com.example.personalfinance.data.mapper;

import com.example.personalfinance.data.local.database.entity.TransactionEntity;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;

public class DataSourceTransactionMapper {
    public static TransactionEntity toEntity(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();

        entity.transactionId = transaction.getId();
        entity.date = transaction.getDateTime();
        entity.type = transaction.getType();
        entity.categoryId = transaction.getCategory().getId();
        entity.amount = transaction.getAmount();

        return entity;
    }

    public static TransactionWithCategoryId toTransactionWithCategoryId(TransactionEntity entity) {
        return new TransactionWithCategoryId(
                entity.transactionId,
                entity.date,
                entity.type,
                entity.categoryId,
                entity.amount
        );
    }
}
