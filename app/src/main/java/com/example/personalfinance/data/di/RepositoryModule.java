package com.example.personalfinance.data.di;

import com.example.personalfinance.data.repository.BudgetRepositoryImpl;
import com.example.personalfinance.data.repository.CategoryRepositoryImpl;
import com.example.personalfinance.data.repository.TransactionRepositoryImpl;
import com.example.personalfinance.domain.repository.BudgetRepository;
import com.example.personalfinance.domain.repository.CategoryRepository;
import com.example.personalfinance.domain.repository.TransactionRepository;

import javax.inject.Singleton;

import dagger.Binds;

public abstract class RepositoryModule {
    @Binds
    @Singleton
    public abstract TransactionRepository bindTransactionRepository(TransactionRepositoryImpl transactionRepositoryImpl);

    @Binds
    @Singleton
    public abstract CategoryRepository bindCategoryRepository(CategoryRepositoryImpl categoryRepository);

    @Binds
    @Singleton
    public abstract BudgetRepository bindBudgetRepository(BudgetRepositoryImpl budgetRepository);
}
