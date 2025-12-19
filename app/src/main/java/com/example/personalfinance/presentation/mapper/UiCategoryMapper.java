package com.example.personalfinance.presentation.mapper;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.presentation.model.CategoryUiModel;

public class UiCategoryMapper {
    public static CategoryUiModel toUi(Category category) {
        return new CategoryUiModel(category.getId(), category.getName());
    }

    public static Category toDomain(CategoryUiModel categoryUiModel, TransactionType type) {
        return new Category(categoryUiModel.getId(), categoryUiModel.getName(), type);
    }
}
