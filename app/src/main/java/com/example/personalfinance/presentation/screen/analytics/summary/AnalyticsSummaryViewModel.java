package com.example.personalfinance.presentation.screen.analytics.summary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.TransactionTypeReport;
import com.example.personalfinance.domain.usecase.analytics.GetTransactionTypeReportUseCase;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AnalyticsSummaryViewModel extends ViewModel {

    private final GetTransactionTypeReportUseCase getReportUseCase;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<SummaryUiState> uiState =
            new MutableLiveData<>(new SummaryUiState());

    @Inject
    public AnalyticsSummaryViewModel(GetTransactionTypeReportUseCase getReportUseCase) {
        this.getReportUseCase = getReportUseCase;
    }

    public LiveData<SummaryUiState> getUiState() {
        return uiState;
    }

    public void updatePeriod(Date startDate, Date endDate) {
        loadData(startDate, endDate);
    }

    private void loadData(Date startDate, Date endDate) {
        executor.execute(() -> {
            try {
                TransactionTypeReport report = getReportUseCase.execute(startDate, endDate, null);

                SummaryUiState newState = new SummaryUiState(
                        report.getIncome(),
                        report.getExpense(),
                        report.getIncome() - report.getExpense(),
                        false,
                        null
                );

                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                uiState.postValue(new SummaryUiState(
                        0, 0, 0, false, "Ошибка загрузки данных"
                ));
            }
        });
    }

    public static class SummaryUiState {
        private final int income;
        private final int expense;
        private final int balance;
        private final boolean loading;
        private final String error;

        public SummaryUiState() {
            this(0, 0, 0, true, null);
        }

        public SummaryUiState(int income, int expense, int balance,
                              boolean loading, String error) {
            this.income = income;
            this.expense = expense;
            this.balance = balance;
            this.loading = loading;
            this.error = error;
        }

        public int getIncome() {
            return income;
        }

        public int getExpense() {
            return expense;
        }

        public int getBalance() {
            return balance;
        }

        public boolean isLoading() {
            return loading;
        }

        public String getError() {
            return error;
        }

        public SummaryUiState copyWithIncome(int income) {
            return new SummaryUiState(income, this.expense, this.balance,
                    this.loading, this.error);
        }

        public SummaryUiState copyWithError(String error) {
            return new SummaryUiState(this.income, this.expense, this.balance,
                    this.loading, error);
        }
    }
}
