package com.example.personalfinance.domain.repository;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransaction(Transaction transaction);
    TransactionWithCategoryId getTransactionWithCategoryId(int id);
    List<TransactionWithCategoryId> getTransactionsWithCategoryIdByType(TransactionType type, int number);
    List<TransactionWithCategoryId> getTransactionsWithCategoryIdForPeriod(Date startDate, Date finishDate);
    int getTransactionTotalAmountByType(TransactionType type,
                                        Date startDate,
                                        Date finishDate,
                                        List<Category> includedCategories);

    Map<Integer, Integer> getTransactionAmountsByCategories(List<Integer> categoryIds,
                                                Date startDate,
                                                Date finishDate);
    boolean existsTransactionByCategory(int categoryId);
}
