package com.example.personalfinance.data.mapper;

import com.example.personalfinance.data.local.database.entity.CategoryEntity;
import com.example.personalfinance.domain.model.Category;

public class DataSourceCategoryMapper {
    public static CategoryEntity toEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();

        entity.categoryId = category.getId();
        entity.name = category.getName();
        entity.type = category.getType();

        return entity;
    }

    public static Category toCategory(CategoryEntity entity) {
        return new Category(
                entity.categoryId,
                entity.name,
                entity.type
        );
    }
}
