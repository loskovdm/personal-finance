package com.example.personalfinance.data.local.database.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.personalfinance.domain.model.TransactionType;

@Entity(tableName = "categories", indices = {@Index(value = {"name", "type"}, unique = true)})
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;
    public String name;
    public TransactionType type;
}
