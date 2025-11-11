package com.example.personalfinance.domain.model;

public class TransactionTypeReport {
    private final int income;
    private final int expense;

    public TransactionTypeReport(int income, int expense) {
        this.income = income;
        this.expense = expense;
    }

    public int getIncome() {
        return income;
    }

    public int getExpense() {
        return expense;
    }
}
