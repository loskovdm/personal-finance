package com.example.personalfinance.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.personalfinance.data.local.database.dao.CategoryDao;
import com.example.personalfinance.data.local.database.dao.TransactionDao;
import com.example.personalfinance.data.local.database.entity.CategoryEntity;
import com.example.personalfinance.data.local.database.entity.TransactionEntity;

@Database(entities = {CategoryEntity.class, TransactionEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract TransactionDao transactionDao();
}
