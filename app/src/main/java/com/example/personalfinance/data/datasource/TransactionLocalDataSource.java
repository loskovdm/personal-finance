package com.example.personalfinance.data.datasource;

import com.example.personalfinance.data.local.database.dao.TransactionDao;
import com.example.personalfinance.data.local.database.entity.TransactionEntity;
import com.example.personalfinance.data.local.database.model.CategoryAmount;
import com.example.personalfinance.data.mapper.DataSourceTransactionMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionWithCategoryId;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class TransactionLocalDataSource {
    private final TransactionDao dao;

    @Inject
    public TransactionLocalDataSource(TransactionDao dao) {
        this.dao = dao;
    }

    public void insertTransaction(Transaction transaction) {
        TransactionEntity entity = DataSourceTransactionMapper.toEntity(transaction);
        dao.insertTransaction(entity);
    }

    public void updateTransaction(Transaction transaction) {
        TransactionEntity entity = DataSourceTransactionMapper.toEntity(transaction);
        dao.updateTransaction(entity);
    }

    public void deleteTransaction(Transaction transaction) {
        TransactionEntity entity = DataSourceTransactionMapper.toEntity(transaction);
        dao.deleteTransaction(entity);
    }

    public TransactionWithCategoryId getTransactionWithCategoryId(int id) {
        TransactionEntity entity = dao.getTransaction(id);
        return DataSourceTransactionMapper.toTransactionWithCategoryId(entity);
    }

    public List<TransactionWithCategoryId> getTransactionsByType(TransactionType type, int number) {
        List<TransactionEntity> entities = dao.getTransactionsByType(type, number);
        return entities.stream()
                .map(DataSourceTransactionMapper::toTransactionWithCategoryId)
                .collect(Collectors.toList());
    }

    public List<TransactionWithCategoryId> getTransactionForPeriod(Date startDate, Date finishDate) {
        List<TransactionEntity> entities = dao.getTransactionForPeriod(startDate, finishDate);
        return entities.stream()
                .map(DataSourceTransactionMapper::toTransactionWithCategoryId)
                .collect(Collectors.toList());
    }

    public int getTransactionTotalAmountByType(
            TransactionType type,
            Date startDate,
            Date finishDate,
            List<Category> includedCategories
    ) {
        List<Integer> includedCategoryIds = null;

        if (includedCategories != null) {
            includedCategoryIds = includedCategories.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
        }

        return dao.getTotalAmountByType(type, startDate, finishDate, includedCategoryIds);
    }

    public List<CategoryAmount> getTransactionAmountsByCategories(
            List<Integer> categoryIds,
            Date startDate,
            Date finishDate
    ) {
        return dao.getAmountsByCategories(categoryIds, startDate, finishDate);
    }

    public boolean existsTransactionByCategory(int categoryId) {
        return dao.existsTransactionByCategory(categoryId);
    }
}
