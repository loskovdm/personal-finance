package com.example.personalfinance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.personalfinance.presentation.screen.analytics.AnalyticsFragment;
import com.example.personalfinance.presentation.screen.category.CategoryFragment;
import com.example.personalfinance.presentation.screen.expenses.ExpenseFragment;
import com.example.personalfinance.presentation.screen.income.IncomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;

            int id = item.getItemId();
            if (id == R.id.nav_expenses) {
                selected = new ExpenseFragment();
            } else if (id == R.id.nav_income) {
                selected = new IncomeFragment();
            } else if (id == R.id.nav_categories) {
                selected = new CategoryFragment();
            } else if (id == R.id.nav_analytics) {
                selected = new AnalyticsFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();

            return true;
        });

        // Установим экран по умолчанию
        bottomNav.setSelectedItemId(R.id.nav_expenses);
    }
}
