package com.example.personalfinance.data.local.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personalfinance.data.local.database.entity.CategoryEntity;
import com.example.personalfinance.domain.model.TransactionType;

import java.util.List;

public interface CategoryDao {
    @Insert
    public void insertCategory(CategoryEntity category);

    @Delete
    public void deleteCategory(CategoryEntity category);

    @Update
    public void updateCategory(CategoryEntity category);

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    public CategoryEntity getCategory(int id);

    @Query("SELECT * FROM categories WHERE type = :type")
    public List<CategoryEntity> getCategoriesByType(TransactionType type);

    @Query("SELECT EXISTS (SELECT 1 FROM categories WHERE name = :name AND type = :type)")
    public boolean existByNameAndType(String name, TransactionType type);
}
