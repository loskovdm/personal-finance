package com.example.personalfinance.presentation.screen.analytics;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.personalfinance.presentation.screen.analytics.summary.AnalyticsSummaryFragment;
import com.example.personalfinance.presentation.screen.analytics.income.AnalyticsIncomeFragment;
import com.example.personalfinance.presentation.screen.analytics.expense.AnalyticsExpenseFragment;

public class AnalyticsPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_COUNT = 3;

    public AnalyticsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AnalyticsSummaryFragment();
            case 1:
                return new AnalyticsExpenseFragment();
            case 2:
                return new AnalyticsIncomeFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return TAB_COUNT;
    }
}