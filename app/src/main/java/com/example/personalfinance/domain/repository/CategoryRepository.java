package com.example.personalfinance.domain.repository;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;

import java.util.List;

public interface CategoryRepository {
    void addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Category category);
    Category getCategory(int id);
    List<Category> getCategoriesByType(TransactionType type);
    boolean existsByNameAndType(String name, TransactionType type);
}
