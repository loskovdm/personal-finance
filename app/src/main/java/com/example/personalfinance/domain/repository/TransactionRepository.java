package com.example.personalfinance.domain.repository;

import com.example.personalfinance.domain.model.ReportRequest;
import com.example.personalfinance.domain.model.ReportResponse;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;

import java.util.List;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransaction(int id);
    Transaction getTransaction(int id);
    List<Transaction> getTransactionsByType(TransactionType type, int number);
    ReportResponse getTransactionReport(ReportRequest reportRequest);
}
