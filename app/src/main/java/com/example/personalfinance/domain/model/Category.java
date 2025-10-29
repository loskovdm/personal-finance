package com.example.personalfinance.domain.model;

public class Category {
    private final int id;
    private final String name;
    private final TransactionType type;

    // For the new category
    public Category(String name, TransactionType type) {
        this.id = 0;
        this.name = name;
        this.type = type;
    }

    // For an existing category
    public Category(int id, String name, TransactionType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public TransactionType getType() {
        return this.type;
    }
}
