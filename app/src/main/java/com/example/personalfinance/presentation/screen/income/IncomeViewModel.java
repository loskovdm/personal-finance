package com.example.personalfinance.presentation.screen.income;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.usecase.analytics.GetMonthSummaryUseCase;
import com.example.personalfinance.domain.usecase.budget.GetCurrentBudgetUseCase;
import com.example.personalfinance.domain.usecase.category.AddCategoryUseCase;
import com.example.personalfinance.domain.usecase.category.GetCategoriesByTypeUseCase;
import com.example.personalfinance.domain.usecase.transaction.AddTransactionUseCase;
import com.example.personalfinance.domain.usecase.transaction.DeleteTransactionUseCase;
import com.example.personalfinance.domain.usecase.transaction.GetTransactionsByTypeUseCase;
import com.example.personalfinance.domain.usecase.transaction.UpdateTransactionUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class IncomeViewModel extends ViewModel {

    private final GetCurrentBudgetUseCase getCurrentBudgetUseCase;
    private final GetMonthSummaryUseCase getMonthSummaryUseCase;
    private final GetCategoriesByTypeUseCase getCategoriesByTypeUseCase;
    private final GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;
    private final AddCategoryUseCase addCategoryUseCase;
    private final AddTransactionUseCase addTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<IncomeUiState> uiState =
            new MutableLiveData<>(new IncomeUiState());

    private int numberLoadedTransaction = 10;

    public LiveData<IncomeUiState> getUiState() { return uiState; }

    @Inject
    public IncomeViewModel(
            GetCurrentBudgetUseCase getCurrentBudgetUseCase,
            GetMonthSummaryUseCase getMonthSummaryUseCase,
            GetCategoriesByTypeUseCase getCategoriesByTypeUseCase,
            GetTransactionsByTypeUseCase getTransactionsByTypeUseCase,
            AddCategoryUseCase addCategoryUseCase,
            AddTransactionUseCase addTransactionUseCase,
            UpdateTransactionUseCase updateTransactionUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase
    ) {
        this.getCurrentBudgetUseCase = getCurrentBudgetUseCase;
        this.getMonthSummaryUseCase = getMonthSummaryUseCase;
        this.getCategoriesByTypeUseCase = getCategoriesByTypeUseCase;
        this.getTransactionsByTypeUseCase = getTransactionsByTypeUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
        this.addTransactionUseCase = addTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    public void loadData() {
        executor.execute(() -> {
            try {
                int budget = getCurrentBudgetUseCase.execute();
                int amount = getMonthSummaryUseCase.execute(TransactionType.INCOME);
                List<Category> categories = getCategoriesByTypeUseCase.execute(TransactionType.INCOME);
                List<Transaction> transactions = getTransactionsByTypeUseCase.execute(TransactionType.INCOME, 10);

                IncomeUiState newState = new IncomeUiState(
                        budget,
                        amount,
                        categories,
                        transactions,
                        null
                );
                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                IncomeUiState current = uiState.getValue();
                uiState.postValue(current != null
                        ? current.copyWithErrorMessage("Ошибка загрузки данных")
                        : new IncomeUiState().copyWithErrorMessage("Ошибка загрузки данных"));
            }
        });
    }

    public void addTransaction(Transaction transaction) {
        executor.execute(() -> {
            addTransactionUseCase.execute(transaction);
            loadData();
        });
    }

    public void updateTransaction(Transaction transaction) {
        executor.execute(() -> {
            updateTransactionUseCase.execute(transaction);
            loadData();
        });
    }

    public void deleteTransaction(Transaction transaction) {
        executor.execute(() -> {
            deleteTransactionUseCase.execute(transaction);
            loadData();
        });
    }

    public void loadTransactions(boolean append) {
        executor.execute(() -> {
            IncomeUiState current = uiState.getValue();
            if (current == null) current = new IncomeUiState();

            List<Transaction> currentTransactions = current.getTransactions() != null
                    ? current.getTransactions()
                    : new ArrayList<>();

            if (append) {
                numberLoadedTransaction = currentTransactions.size() + 10;
            } else {
                numberLoadedTransaction = 10;
            }

            List<Transaction> transactions = getTransactionsByTypeUseCase.execute(
                    TransactionType.INCOME,
                    numberLoadedTransaction
            );

            IncomeUiState newState = new IncomeUiState(
                    current.getBudget(),
                    current.getAmountByMonth(),
                    current.getCategories(),
                    transactions,
                    current.getErrorMessage()
            );

            uiState.postValue(newState);
        });
    }



    public void clearError() {
        IncomeUiState current = uiState.getValue();
        if (current == null) return;

        IncomeUiState newState = new IncomeUiState(
                current.getBudget(),
                current.getAmountByMonth(),
                current.getCategories(),
                current.getTransactions(),
                null
        );
        uiState.postValue(newState);
    }
}
