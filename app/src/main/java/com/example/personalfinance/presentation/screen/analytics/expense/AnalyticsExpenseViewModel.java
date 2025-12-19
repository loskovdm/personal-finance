package com.example.personalfinance.presentation.screen.analytics.expense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.CategoryReport;
import com.example.personalfinance.domain.model.CategoryValue;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.usecase.analytics.GetCategoryReportUseCase;
import com.example.personalfinance.domain.usecase.category.GetCategoriesByTypeUseCase;

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
public class AnalyticsExpenseViewModel extends ViewModel {

    private final GetCategoryReportUseCase getCategoryReportUseCase;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<ExpenseAnalyticsUiState> uiState =
            new MutableLiveData<>(new ExpenseAnalyticsUiState());

    @Inject
    public AnalyticsExpenseViewModel(GetCategoryReportUseCase getCategoryReportUseCase) {
        this.getCategoryReportUseCase = getCategoryReportUseCase;
    }

    public LiveData<ExpenseAnalyticsUiState> getUiState() {
        return uiState;
    }

    public void loadData(Date startDate, Date endDate) {
        executor.execute(() -> {
            try {
                CategoryReport report = getCategoryReportUseCase.execute(
                        TransactionType.EXPENSE,
                        startDate,
                        endDate,
                        null // Все категории
                );

                List<CategoryValue> sortedValues = new ArrayList<>(report.getCategoryValues());
                Collections.sort(sortedValues, new Comparator<CategoryValue>() {
                    @Override
                    public int compare(CategoryValue cv1, CategoryValue cv2) {
                        return Integer.compare(cv2.getAmount(), cv1.getAmount());
                    }
                });

                ExpenseAnalyticsUiState newState = new ExpenseAnalyticsUiState(
                        report.getTotalAmount(),
                        sortedValues,
                        false,
                        null
                );

                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                uiState.postValue(new ExpenseAnalyticsUiState(
                        0,
                        new ArrayList<>(),
                        false,
                        "Ошибка загрузки данных"
                ));
            }
        });
    }

    public static class ExpenseAnalyticsUiState {
        private final int totalAmount;
        private final List<CategoryValue> categoryValues;
        private final boolean loading;
        private final String error;

        public ExpenseAnalyticsUiState() {
            this(0, new ArrayList<>(), true, null);
        }

        public ExpenseAnalyticsUiState(int totalAmount,
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