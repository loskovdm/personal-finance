package com.example.personalfinance.domain.model;

import java.util.List;

public class ReportResponse {
    private final int totalAmount;
    private final List<CategoryValue> categoryValues;

    public ReportResponse(int totalAmount, List<CategoryValue> categoryValues) {
        this.totalAmount = totalAmount;
        this.categoryValues = categoryValues;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public List<CategoryValue> getCategoryValues() {
        return categoryValues;
    }
}
