package com.example.personalfinance.data.local.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personalfinance.data.local.database.entity.TransactionEntity;
import com.example.personalfinance.data.local.database.model.CategoryAmount;
import com.example.personalfinance.domain.model.TransactionType;

import java.util.Date;
import java.util.List;

public interface TransactionDao {
    @Insert
    public void insertTransaction(TransactionEntity transaction);

    @Update
    public void updateTransaction(TransactionEntity transaction);

    @Delete
    public void deleteTransaction(TransactionEntity transaction);

    @Query("SELECT * " +
            "FROM transactions " +
            "WHERE transactionId = :id")
    public TransactionEntity getTransaction(int id);

    @Query("SELECT * " +
            "FROM transactions " +
            "WHERE type = :type " +
            "ORDER BY date DESC " +
            "LIMIT :number")
    public List<TransactionEntity> getTransactionsByType(TransactionType type, int number);

    @Query("SELECT SUM(amount) " +
            "FROM transactions " +
            "WHERE type = :type " +
            "AND date >= :startDate " +
            "AND date <= :finishDate " +
            "AND (:includedCategoryIds IS NULL OR categoryId IN (:includedCategoryIds))")
    public int getTotalAmountByType(TransactionType type,
                                    Date startDate,
                                    Date finishDate,
                                    List<Integer> includedCategoryIds);

    @Query("SELECT * " +
            "FROM transactions " +
            "WHERE (:startDate IS NULL OR date >= :startDate) " +
            "AND (:finishDate IS NULL OR date <= :finishDate)")
    public List<TransactionEntity> getTransactionForPeriod(Date startDate, Date finishDate);

    @Query("SELECT categoryId AS categoryId, SUM(amount) AS amount " +
            "FROM transactions " +
            "WHERE date >= :startDate " +
            "AND date <= :finishDate " +
            "AND (:categoryIds IS NULL OR categoryId IN (:categoryIds))" +
            "GROUP BY categoryId")
    public List<CategoryAmount> getAmountsByCategories(List<Integer> categoryIds,
                                                       Date startDate,
                                                       Date finishDate);

    @Query("SELECT EXISTS(SELECT 1 FROM transactions WHERE categoryId = :categoryId)")
    public boolean existsTransactionByCategory(int categoryId);
}
