package com.example.personalfinance.presentation.screen.category;

import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.presentation.model.CategoryUiModel;

import java.util.List;

public class CategoryUiState {

    private final boolean isLoading;
    private final TransactionType type;
    private final List<CategoryUiModel> categories;
    private final String errorMessage;

    public CategoryUiState() {
        this.isLoading = false;
        this.type = TransactionType.EXPENSE;
        this.categories = List.of();
        this.errorMessage = null;
    }

    public CategoryUiState(boolean isLoading,
                           TransactionType type,
                           List<CategoryUiModel> categories,
                           String errorMessage) {
        this.isLoading = isLoading;
        this.type = type;
        this.categories = categories;
        this.errorMessage = errorMessage;
    }

    public CategoryUiState copy(TransactionType type,
                                boolean isLoading,
                                List<CategoryUiModel> categories,
                                String errorMessage) {
        return new CategoryUiState(isLoading, type, categories, errorMessage);
    }

    public boolean isLoading() { return isLoading; }
    public TransactionType getType() { return type; }
    public List<CategoryUiModel> getCategories() { return categories; }
    public String getErrorMessage() { return errorMessage; }
}

