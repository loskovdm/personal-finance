package com.example.personalfinance.presentation.screen.expenses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.usecase.analytics.GetMonthSummaryUseCase;
import com.example.personalfinance.domain.usecase.budget.GetCurrentBudgetUseCase;
import com.example.personalfinance.domain.usecase.category.GetCategoriesByTypeUseCase;
import com.example.personalfinance.domain.usecase.transaction.AddTransactionUseCase;
import com.example.personalfinance.domain.usecase.transaction.DeleteTransactionUseCase;
import com.example.personalfinance.domain.usecase.transaction.GetTransactionsByTypeUseCase;
import com.example.personalfinance.domain.usecase.transaction.UpdateTransactionUseCase;
import com.example.personalfinance.presentation.screen.error.ErrorEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ExpenseViewModel extends ViewModel {

    private final GetCurrentBudgetUseCase getCurrentBudgetUseCase;
    private final GetMonthSummaryUseCase getMonthSummaryUseCase;
    private final GetCategoriesByTypeUseCase getCategoriesByTypeUseCase;
    private final GetTransactionsByTypeUseCase getTransactionsByTypeUseCase;
    private final AddTransactionUseCase addTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<ExpenseUiState> uiState =
            new MutableLiveData<>(new ExpenseUiState());

    private final MutableLiveData<ErrorEvent> errorEvent = new MutableLiveData<>();

    private int numberLoadedTransaction = 10;

    public LiveData<ExpenseUiState> getUiState() { return uiState; }

    @Inject
    public ExpenseViewModel(
            GetCurrentBudgetUseCase getCurrentBudgetUseCase,
            GetMonthSummaryUseCase getMonthSummaryUseCase,
            GetCategoriesByTypeUseCase getCategoriesByTypeUseCase,
            GetTransactionsByTypeUseCase getTransactionsByTypeUseCase,
            AddTransactionUseCase addTransactionUseCase,
            UpdateTransactionUseCase updateTransactionUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase
    ) {
        this.getCurrentBudgetUseCase = getCurrentBudgetUseCase;
        this.getMonthSummaryUseCase = getMonthSummaryUseCase;
        this.getCategoriesByTypeUseCase = getCategoriesByTypeUseCase;
        this.getTransactionsByTypeUseCase = getTransactionsByTypeUseCase;
        this.addTransactionUseCase = addTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    public void loadData() {
        executor.execute(() -> {
            try {
                int budget = getCurrentBudgetUseCase.execute();
                int amount = getMonthSummaryUseCase.execute(TransactionType.EXPENSE);
                List<Category> categories = getCategoriesByTypeUseCase.execute(TransactionType.EXPENSE);
                List<Transaction> transactions = getTransactionsByTypeUseCase.execute(TransactionType.EXPENSE, 10);

                ExpenseUiState newState = new ExpenseUiState(
                        budget,
                        amount,
                        categories,
                        transactions,
                        null
                );
                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                ExpenseUiState current = uiState.getValue();
                uiState.postValue(current != null
                        ? current.copyWithErrorMessage("Ошибка загрузки данных")
                        : new ExpenseUiState().copyWithErrorMessage("Ошибка загрузки данных"));
            }
        });
    }

    public void addTransaction(Transaction transaction) {
        executor.execute(() -> {
            try {
                addTransactionUseCase.execute(transaction);
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
                errorEvent.postValue(new ErrorEvent(
                        "Ошибка добавления транзакции расхода",
                        null
                ));
            }
        });
    }

    public void updateTransaction(Transaction transaction) {
        executor.execute(() -> {
            try {
                updateTransactionUseCase.execute(transaction);
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
                errorEvent.postValue(new ErrorEvent(
                        "Ошибка изменения транзакции расхода",
                        null
                ));
            }
        });
    }

    public void deleteTransaction(Transaction transaction) {
        executor.execute(() -> {
            try {
                deleteTransactionUseCase.execute(transaction);
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
                errorEvent.postValue(new ErrorEvent(
                        "Ошибка удаления транзакции расхода",
                        null
                ));
            }
        });
    }

    public void loadTransactions(boolean append) {
        executor.execute(() -> {
            try {
                ExpenseUiState current = uiState.getValue();
                if (current == null) current = new ExpenseUiState();

                List<Transaction> currentTransactions = current.getTransactions() != null
                        ? current.getTransactions()
                        : new ArrayList<>();

                if (append) {
                    numberLoadedTransaction = currentTransactions.size() + 10;
                } else {
                    numberLoadedTransaction = 10;
                }

                List<Transaction> transactions = getTransactionsByTypeUseCase.execute(
                        TransactionType.EXPENSE,
                        numberLoadedTransaction
                );

                ExpenseUiState newState = new ExpenseUiState(
                        current.getBudget(),
                        current.getAmountByMonth(),
                        current.getCategories(),
                        transactions,
                        current.getErrorMessage()
                );

                uiState.postValue(newState);
            } catch (Exception e) {
                e.printStackTrace();
                errorEvent.postValue(new ErrorEvent(
                        "Ошибка загрузки транзакций расходов",
                        null
                ));
            }
        });
    }

    public void clearError() {
        ExpenseUiState current = uiState.getValue();
        if (current == null) return;

        ExpenseUiState newState = new ExpenseUiState(
                current.getBudget(),
                current.getAmountByMonth(),
                current.getCategories(),
                current.getTransactions(),
                null
        );
        uiState.postValue(newState);
    }

    public LiveData<ErrorEvent> getErrorEvent() { return errorEvent; }
}
