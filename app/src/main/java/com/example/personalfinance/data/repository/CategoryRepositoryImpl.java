package com.example.personalfinance.data.repository;

import com.example.personalfinance.data.datasource.CategoryLocalDataSource;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.CategoryRepository;

import java.util.List;

import javax.inject.Inject;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryLocalDataSource dataSource;

    @Inject
    public CategoryRepositoryImpl(CategoryLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void addCategory(Category category) {
        dataSource.addCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        dataSource.updateCategory(category);
    }

    @Override
    public void deleteCategory(Category category) {
        dataSource.deleteCategory(category);
    }

    @Override
    public Category getCategory(int id) {
        return dataSource.getCategory(id);
    }

    @Override
    public List<Category> getCategoriesByType(TransactionType type) {
        return dataSource.getCategoriesByType(type);
    }

    @Override
    public boolean existsByNameAndType(String name, TransactionType type) {
        return dataSource.existByNameAndType(name, type);
    }
}
