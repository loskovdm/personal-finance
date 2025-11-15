package com.example.personalfinance.data.datasource;

import com.example.personalfinance.data.local.database.dao.CategoryDao;
import com.example.personalfinance.data.mapper.DataSourceCategoryMapper;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class CategoryLocalDataSource {
    private final CategoryDao dao;

    @Inject
    public CategoryLocalDataSource(CategoryDao dao) {
        this.dao = dao;
    }

    public void addCategory(Category category) {
        dao.insertCategory(DataSourceCategoryMapper.toEntity(category));
    }

    public void deleteCategory(Category category) {
        dao.deleteCategory(DataSourceCategoryMapper.toEntity(category));
    }

    public void updateCategory(Category category) {
        dao.updateCategory(DataSourceCategoryMapper.toEntity(category));
    }

    public Category getCategory(int categoryId) {
        return DataSourceCategoryMapper.toCategory(dao.getCategory(categoryId));
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        return dao.getCategoriesByType(type).stream()
                .map(DataSourceCategoryMapper::toCategory)
                .collect(Collectors.toList());
    }

    public boolean existByNameAndType(String name, TransactionType type) {
        return dao.existByNameAndType(name, type);
    }
}
