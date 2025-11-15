package com.example.personalfinance.data.di;

import android.content.Context;

import androidx.room.Room;

import com.example.personalfinance.data.local.database.AppDatabase;
import com.example.personalfinance.data.local.database.dao.CategoryDao;
import com.example.personalfinance.data.local.database.dao.TransactionDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "personal_finance_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public CategoryDao provideCategoryDao(AppDatabase db) {
        return db.categoryDao();
    }

    @Provides
    public TransactionDao provideTransactionDao(AppDatabase db) {
        return db.transactionDao();
    }
}
