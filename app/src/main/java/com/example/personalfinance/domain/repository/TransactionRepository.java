package com.example.personalfinance.domain.repository;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.CategoryReport;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.model.TransactionTypeReport;

import java.util.Date;
import java.util.List;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransaction(int id);
    Transaction getTransaction(int id);
    List<Transaction> getTransactionsByType(TransactionType type, int number);
    TransactionTypeReport getTransactionTypeReport(Date startDate,
                                                   Date finishDate,
                                                   List<Category> includedCategories);
    CategoryReport getTransactionCategoryReport(TransactionType type,
                                                Date startDate,
                                                Date finishDate,
                                                List<Category> includedCategories);
    boolean existsTransactionByCategory(int id);
}
