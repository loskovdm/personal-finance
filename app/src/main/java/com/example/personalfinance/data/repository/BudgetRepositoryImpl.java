package com.example.personalfinance.data.repository;

import com.example.personalfinance.data.datasource.BudgetLocalDataSource;
import com.example.personalfinance.domain.repository.BudgetRepository;

import javax.inject.Inject;

public class BudgetRepositoryImpl implements BudgetRepository {
    private final BudgetLocalDataSource dataSource;

    @Inject
    public BudgetRepositoryImpl(BudgetLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getBudget() {
        return dataSource.read();
    }

    @Override
    public void setBudget(int amount) {
        dataSource.write(amount);
    }
}
