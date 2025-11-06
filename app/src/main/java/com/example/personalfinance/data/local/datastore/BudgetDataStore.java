package com.example.personalfinance.data.local.datastore;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;

@Singleton
public class BudgetDataStore {
    private final RxDataStore<Preferences> rxDataStore;
    private static final Preferences.Key<Integer> BUDGET_KEY = PreferencesKeys.intKey("budget_amount");

    @Inject
    public BudgetDataStore(RxDataStore<Preferences> rxDataStore) {
        this.rxDataStore = rxDataStore;
    }

    public RxDataStore<Preferences> getDataStore() {
        return rxDataStore;
    }

    public int read() {
        return rxDataStore.data()
                .firstOrError()
                .map(prefs -> {
                    Integer value = prefs.get(BUDGET_KEY);
                    return value != null ? value : 0;
                })
                .blockingGet();
    }

    public void write(int amount) {
        Preferences result = rxDataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(BUDGET_KEY, amount);
            return Single.just(mutablePreferences);
        }).blockingGet();
    }
}
