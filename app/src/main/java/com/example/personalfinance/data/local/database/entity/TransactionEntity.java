package com.example.personalfinance.data.local.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.personalfinance.domain.model.TransactionType;

import java.util.Date;

@Entity(
        tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = CategoryEntity.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = {@Index("categoryId")}
)
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    public int transactionId;
    public Date date;
    public TransactionType type;
    public int categoryId;
    public int amount;
}
