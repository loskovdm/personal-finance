package com.example.personalfinance.presentation.screen.income;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class IncomeUiState {

    private final int budget;
    private final int amountByMonth;
    private final List<Category> categories;
    private final List<Transaction> transactions;
    private final String errorMessage;

    // Конструктор по умолчанию
    public IncomeUiState() {
        this.budget = 0;
        this.amountByMonth = 0;
        this.categories = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.errorMessage = null;
    }

    // Конструктор с параметрами
    public IncomeUiState(int budget,
                         int amountByMonth,
                         List<Category> categories,
                         List<Transaction> transactions,
                         String errorMessage) {
        this.budget = budget;
        this.amountByMonth = amountByMonth;
        this.categories = categories != null ? categories : new ArrayList<>();
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        this.errorMessage = errorMessage;
    }

    public int getBudget() {
        return budget;
    }

    public int getAmountByMonth() {
        return amountByMonth;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public IncomeUiState copyWithBudget(int budget) {
        return new IncomeUiState(
                budget,
                this.amountByMonth,
                this.categories,
                this.transactions,
                this.errorMessage
        );
    }

    public IncomeUiState copyWithAmountByMonth(int amountByMonth) {
        return new IncomeUiState(
                this.budget,
                amountByMonth,
                this.categories,
                this.transactions,
                this.errorMessage
        );
    }

    public IncomeUiState copyWithCategories(List<Category> categories) {
        return new IncomeUiState(
                this.budget,
                this.amountByMonth,
                categories,
                this.transactions,
                this.errorMessage
        );
    }

    public IncomeUiState copyWithTransactions(List<Transaction> transactions) {
        return new IncomeUiState(
                this.budget,
                this.amountByMonth,
                this.categories,
                transactions,
                this.errorMessage
        );
    }

    public IncomeUiState copyWithErrorMessage(String errorMessage) {
        return new IncomeUiState(
                this.budget,
                this.amountByMonth,
                this.categories,
                this.transactions,
                errorMessage
        );
    }
}
