package com.example.personalfinance.domain.model;

import java.util.Date;

public class TransactionWithCategoryId {
    private final int id;
    private final Date date;
    private final TransactionType type;
    private final int categoryId;
    private final int amount;

    public TransactionWithCategoryId(
            int id,
            Date dateTime,
            TransactionType type,
            int categoryId,
            int amount
    ) {
        this.id = id;
        this.date = dateTime;
        this.type = type;
        this.categoryId = categoryId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getAmount() {
        return amount;
    }
}
