package com.example.personalfinance.data.datasource;

import com.example.personalfinance.data.local.datastore.BudgetDataStore;

import javax.inject.Inject;

public class BudgetLocalDataSource {
    private final BudgetDataStore dataStore;

    @Inject
    public BudgetLocalDataSource(BudgetDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public int read() {
        return dataStore.read();
    }

    public void write(int amount) {
        dataStore.write(amount);
    }
}
