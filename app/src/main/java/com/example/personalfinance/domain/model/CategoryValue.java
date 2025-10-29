package com.example.personalfinance.domain.model;

public class CategoryValue {
    private final Category category;
    private final int amount;

    public CategoryValue(Category category, int amount) {
        this.category = category;
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }
}
