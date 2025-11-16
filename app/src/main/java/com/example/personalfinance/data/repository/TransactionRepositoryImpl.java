package com.example.personalfinance.data.repository;

import com.example.personalfinance.data.datasource.TransactionLocalDataSource;
import com.example.personalfinance.data.local.database.model.CategoryAmount;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionLocalDataSource dataSource;

    @Inject
    public TransactionRepositoryImpl(TransactionLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        dataSource.insertTransaction(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        dataSource.updateTransaction(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        dataSource.deleteTransaction(transaction);
    }

    @Override
    public TransactionWithCategoryId getTransactionWithCategoryId(int id) {
        return dataSource.getTransactionWithCategoryId(id);
    }

    @Override
    public List<TransactionWithCategoryId> getTransactionsWithCategoryIdByType(TransactionType type, int number) {
        return dataSource.getTransactionsByType(type, number);
    }

    @Override
    public List<TransactionWithCategoryId> getTransactionsWithCategoryIdForPeriod(Date startDate, Date finishDate) {
        return dataSource.getTransactionForPeriod(startDate, finishDate);
    }

    @Override
    public int getTransactionTotalAmountByType(TransactionType type, Date startDate, Date finishDate, List<Category> includedCategories) {
        return dataSource.getTransactionTotalAmountByType(type, startDate, finishDate, includedCategories);
    }

    @Override
    public Map<Integer, Integer> getTransactionAmountsByCategories(
            List<Integer> categoryIds,
            Date startDate,
            Date finishDate
    ) {
        List<CategoryAmount> categoryAmounts = dataSource.getTransactionAmountsByCategories(
                categoryIds,
                startDate,
                finishDate
        );

        return categoryAmounts.stream()
                .collect(Collectors.toMap(
                        ca -> ca.categoryId,
                        ca -> ca.amount
                ));
    }

    @Override
    public boolean existsTransactionByCategory(int categoryId) {
        return dataSource.existsTransactionByCategory(categoryId);
    }
}
