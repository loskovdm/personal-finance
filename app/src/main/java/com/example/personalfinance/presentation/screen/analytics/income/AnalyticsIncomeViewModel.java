package com.example.personalfinance.presentation.screen.analytics.income;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.CategoryReport;
import com.example.personalfinance.domain.model.CategoryValue;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.usecase.analytics.GetCategoryReportUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnalyticsIncomeViewModel extends ViewModel {

    private final GetCategoryReportUseCase getCategoryReportUseCase;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<IncomeAnalyticsUiState> uiState =
            new MutableLiveData<>(new IncomeAnalyticsUiState());

    @Inject
    public AnalyticsIncomeViewModel(GetCategoryReportUseCase getCategoryReportUseCase) {
        this.getCategoryReportUseCase = getCategoryReportUseCase;
    }

    public LiveData<IncomeAnalyticsUiState> getUiState() {
        return uiState;
    }

    public void loadData(Date startDate, Date endDate) {
        executor.execute(() -> {
            try {
                CategoryReport report = getCategoryReportUseCase.execute(
                        TransactionType.INCOME,
                        startDate,
                        endDate,
                        null // Все категории
                );

                List<CategoryValue> sortedValues = new ArrayList<>(report.getCategoryValues());
                Collections.sort(sortedValues, new Comparator<CategoryValue>() {
                    @Override
                    public int compare(CategoryValue cv1, CategoryValue cv2) {
                        return Integer.compare(cv2.getAmount(), cv1.getAmount()); // По убыванию
                    }
                });

                IncomeAnalyticsUiState newState = new IncomeAnalyticsUiState(
                        report.getTotalAmount(),
                        sortedValues,
                        false,
                        null
                );

                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                uiState.postValue(new IncomeAnalyticsUiState(
                        0,
                        new ArrayList<>(),
                        false,
                        "Ошибка загрузки данных"
                ));
            }
        });
    }

    // UI State класс
    public static class IncomeAnalyticsUiState {
        private final int totalAmount;
        private final List<CategoryValue> categoryValues;
        private final boolean loading;
        private final String error;

        public IncomeAnalyticsUiState() {
            this(0, new ArrayList<>(), true, null);
        }

        public IncomeAnalyticsUiState(int totalAmount,
                                      List<CategoryValue> categoryValues,
                                      boolean loading,
                                      String error) {
            this.totalAmount = totalAmount;
            this.categoryValues = categoryValues != null ? categoryValues : new ArrayList<>();
            this.loading = loading;
            this.error = error;
        }

        public int getTotalAmount() { return totalAmount; }
        public List<CategoryValue> getCategoryValues() { return categoryValues; }
        public boolean isLoading() { return loading; }
        public String getError() { return error; }
    }
}