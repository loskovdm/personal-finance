package com.example.personalfinance.presentation.screen.analytics.summary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalfinance.R;
import com.example.personalfinance.presentation.screen.analytics.AnalyticsSharedViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsSummaryFragment extends Fragment {

    private AnalyticsSummaryViewModel viewModel;

    private BarChart barChart;
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;
    private TextView tvBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AnalyticsSummaryViewModel.class);

        initViews(view);
        setupChart();
        observeViewModel();

        AnalyticsSharedViewModel sharedViewModel =
                new ViewModelProvider(requireActivity()).get(AnalyticsSharedViewModel.class);

        sharedViewModel.getPeriodLiveData().observe(getViewLifecycleOwner(), period -> {
            if (period != null) {
                viewModel.updatePeriod(period.startDate, period.endDate);
            }
        });
    }

    private void initViews(View view) {
        barChart = view.findViewById(R.id.chartSummary);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
    }

    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setPinchZoom(false);
        barChart.setDrawBorders(false);
        barChart.setExtraOffsets(10, 10, 10, 10);

        barChart.getLegend().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(2);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) return "Доходы";
                if (value == 1) return "Расходы";
                return "";
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f ₽", value);
            }
        });

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void observeViewModel() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            tvTotalIncome.setText(String.format("%d ₽", state.getIncome()));
            tvTotalExpense.setText(String.format("%d ₽", state.getExpense()));

            int balance = state.getBalance();
            tvBalance.setText(String.format("%d ₽", balance));

            if (balance >= 0) {
                tvBalance.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvBalance.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            updateChart(state.getIncome(), state.getExpense());
        });
    }

    private void updateChart(int income, int expense) {
        List<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0, income));
        entries.add(new BarEntry(1, expense));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(
                Color.parseColor("#4CAF50"), // Зеленый для доходов
                Color.parseColor("#F44336")  // Красный для расходов
        );
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f ₽", value);
            }
        });

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f); // Ширина столбцов

        barChart.setData(barData);
        barChart.invalidate(); // Обновляем график
        barChart.animateY(1000); // Анимация
    }
}
