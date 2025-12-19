package com.example.personalfinance.presentation.screen.expenses;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ExpenseUiState {

    private final int budget;
    private final int amountByMonth;
    private final List<Category> categories;
    private final List<Transaction> transactions;
    private final String errorMessage;

    // Конструктор по умолчанию
    public ExpenseUiState() {
        this.budget = 0;
        this.amountByMonth = 0;
        this.categories = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.errorMessage = null;
    }

    // Конструктор с параметрами
    public ExpenseUiState(int budget,
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

    public ExpenseUiState copyWithBudget(int budget) {
        return new ExpenseUiState(
                budget,
                this.amountByMonth,
                this.categories,
                this.transactions,
                this.errorMessage
        );
    }

    public ExpenseUiState copyWithAmountByMonth(int amountByMonth) {
        return new ExpenseUiState(
                this.budget,
                amountByMonth,
                this.categories,
                this.transactions,
                this.errorMessage
        );
    }

    public ExpenseUiState copyWithCategories(List<Category> categories) {
        return new ExpenseUiState(
                this.budget,
                this.amountByMonth,
                categories,
                this.transactions,
                this.errorMessage
        );
    }

    public ExpenseUiState copyWithTransactions(List<Transaction> transactions) {
        return new ExpenseUiState(
                this.budget,
                this.amountByMonth,
                this.categories,
                transactions,
                this.errorMessage
        );
    }

    public ExpenseUiState copyWithErrorMessage(String errorMessage) {
        return new ExpenseUiState(
                this.budget,
                this.amountByMonth,
                this.categories,
                this.transactions,
                errorMessage
        );
    }
}
