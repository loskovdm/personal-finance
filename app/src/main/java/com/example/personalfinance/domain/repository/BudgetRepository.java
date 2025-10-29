package com.example.personalfinance.domain.repository;

import com.example.personalfinance.domain.model.TransactionType;

import java.util.Date;

public interface BudgetRepository {
    public int getCurrentBudget();
    public int getBudgetForPeriod(Date startDate, Date finishData);
    public void updateBudget(TransactionType type, int amount);
}
