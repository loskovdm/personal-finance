package com.example.personalfinance.domain.model;

import java.util.Date;

public class Transaction {
    private final int id;
    private final Date dateTime;
    private final TransactionType type;
    private final Category category;
    private final int amount;

    // For the new transaction
    public Transaction(
            Date dateTime,
            TransactionType type,
            Category category,
            int amount
    ) {
        this.id = 0;
        this.dateTime = dateTime;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }

    // For an existing transaction
    public Transaction(
        int id,
        Date dateTime,
        TransactionType type,
        Category category,
        int amount
    ) {
        this.id = id;
        this.dateTime = dateTime;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }
}
