package com.example.personalfinance.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.personalfinance.data.local.database.converter.DateConverter;
import com.example.personalfinance.data.local.database.dao.CategoryDao;
import com.example.personalfinance.data.local.database.dao.TransactionDao;
import com.example.personalfinance.data.local.database.entity.CategoryEntity;
import com.example.personalfinance.data.local.database.entity.TransactionEntity;

@Database(entities = {CategoryEntity.class, TransactionEntity.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract TransactionDao transactionDao();
}
