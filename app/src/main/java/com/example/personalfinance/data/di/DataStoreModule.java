package com.example.personalfinance.data.di;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataStoreModule {
    @Provides
    @Singleton
    public static RxDataStore<Preferences> provideRxDataStore(@ApplicationContext Context context) {
        return new RxPreferenceDataStoreBuilder(context, "budget_prefs").build();
    }
}
