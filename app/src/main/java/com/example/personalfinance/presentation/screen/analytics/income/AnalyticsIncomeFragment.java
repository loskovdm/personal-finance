package com.example.personalfinance.presentation.screen.analytics.income;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalfinance.R;
import com.example.personalfinance.domain.model.CategoryValue;
import com.example.personalfinance.presentation.screen.analytics.AnalyticsSharedViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsIncomeFragment extends Fragment {

    private AnalyticsIncomeViewModel viewModel;
    private AnalyticsSharedViewModel sharedViewModel;

    private PieChart pieChart;
    private TextView tvTotalIncome;
    private TextView tvNoData;
    private TextView tvDetailsTitle;
    private LinearLayout llCategoryDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AnalyticsIncomeViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(AnalyticsSharedViewModel.class);

        initViews(view);
        setupPieChart();
        observeViewModels();
    }

    private void initViews(View view) {
        pieChart = view.findViewById(R.id.chartIncome);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvNoData = view.findViewById(R.id.tvNoData);
        tvDetailsTitle = view.findViewById(R.id.tvDetailsTitle);
        llCategoryDetails = view.findViewById(R.id.llCategoryDetails);
    }

    private void setupPieChart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
    }

    private void observeViewModels() {
        sharedViewModel.getPeriodLiveData().observe(getViewLifecycleOwner(), period -> {
            if (period != null) {
                viewModel.loadData(period.startDate, period.endDate);
            }
        });

        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            tvTotalIncome.setText(String.format("Общие доходы: %d ₽", state.getTotalAmount()));

            if (state.getCategoryValues().isEmpty()) {
                tvNoData.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                tvDetailsTitle.setVisibility(View.GONE);
                llCategoryDetails.setVisibility(View.GONE);
            } else {
                tvNoData.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
                tvDetailsTitle.setVisibility(View.VISIBLE);
                llCategoryDetails.setVisibility(View.VISIBLE);
                updatePieChart(state.getCategoryValues());
                updateCategoryDetails(state.getCategoryValues(), state.getTotalAmount());
            }
        });
    }

    private void updatePieChart(List<CategoryValue> categoryValues) {
        List<PieEntry> entries = new ArrayList<>();

        for (CategoryValue categoryValue : categoryValues) {
            if (categoryValue.getAmount() > 0) {
                entries.add(new PieEntry(
                        categoryValue.getAmount(),
                        categoryValue.getCategory().getName()
                ));
            }
        }

        if (entries.isEmpty()) {
            pieChart.clear();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(getChartColors(entries.size()));
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f ₽", value);
            }
        });

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1000);
    }

    private void updateCategoryDetails(List<CategoryValue> categoryValues, int totalAmount) {
        llCategoryDetails.removeAllViews();

        if (categoryValues.isEmpty() || totalAmount == 0) {
            return;
        }

        for (int i = 0; i < categoryValues.size(); i++) {
            CategoryValue categoryValue = categoryValues.get(i);
            if (categoryValue.getAmount() > 0) {
                View detailItem = createCategoryDetailItem(categoryValue, totalAmount, i);
                llCategoryDetails.addView(detailItem);
            }
        }
    }

    private View createCategoryDetailItem(CategoryValue categoryValue, int totalAmount, int position) {
        View itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_analytics_category, llCategoryDetails, false);

        TextView tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        TextView tvCategoryAmount = itemView.findViewById(R.id.tvCategoryAmount);
        TextView tvCategoryPercentage = itemView.findViewById(R.id.tvCategoryPercentage);
        View viewCategoryColor = itemView.findViewById(R.id.viewCategoryColor);

        tvCategoryName.setText(categoryValue.getCategory().getName());
        tvCategoryAmount.setText(String.format("%d ₽", categoryValue.getAmount()));

        float percentage = (categoryValue.getAmount() * 100f) / totalAmount;
        tvCategoryPercentage.setText(String.format("%.1f%%", percentage));

        List<Integer> colors = getChartColors(position + 1);
        int color = colors.get(position % colors.size());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);
        drawable.setSize(24, 24);
        viewCategoryColor.setBackground(drawable);

        return itemView;
    }

    private List<Integer> getChartColors(int count) {
        List<Integer> colors = new ArrayList<>();

        int[] baseColors = {
                Color.parseColor("#4CAF50"),  // Зеленый
                Color.parseColor("#2196F3"),  // Синий
                Color.parseColor("#9C27B0"),  // Фиолетовый
                Color.parseColor("#FF9800"),  // Оранжевый
                Color.parseColor("#009688"),  // Бирюзовый
                Color.parseColor("#E91E63"),  // Розовый
                Color.parseColor("#3F51B5"),  // Индиго
                Color.parseColor("#00BCD4"),  // Голубой
                Color.parseColor("#8BC34A"),  // Светло-зеленый
                Color.parseColor("#FF5722"),  // Глубокий оранжевый
                Color.parseColor("#795548"),  // Коричневый
                Color.parseColor("#607D8B")   // Синий-серый
        };

        for (int i = 0; i < count; i++) {
            colors.add(baseColors[i % baseColors.length]);
        }

        return colors;
    }
}