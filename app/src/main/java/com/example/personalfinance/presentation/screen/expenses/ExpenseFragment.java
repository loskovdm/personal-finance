package com.example.personalfinance.presentation.screen.expenses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalfinance.R;
import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.Transaction;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.presentation.screen.util.TransactionAdapter;
import com.example.personalfinance.presentation.screen.error.ErrorDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExpenseFragment extends Fragment {

    private ExpenseViewModel viewModel;

    private TextView tvBudget;
    private TextView tvMonthExpense;
    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private Button btnLoadMore;
    private FloatingActionButton fabAdd;

    private static final TransactionType TRANSACTION_TYPE = TransactionType.EXPENSE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        initViews(view);
        setupRecycler();
        setupListeners();
        observeUiState();

        viewModel.loadData();
    }

    private void initViews(View view) {
        tvBudget = view.findViewById(R.id.tvBudget);
        tvMonthExpense = view.findViewById(R.id.tvMonthExpense);
        rvTransactions = view.findViewById(R.id.rvTransactions);
        btnLoadMore = view.findViewById(R.id.btnLoadMore);
        fabAdd = view.findViewById(R.id.fabAddExpense);
    }

    private void setupRecycler() {
        adapter = new TransactionAdapter(new ArrayList<>(), this::openTransactionDialog);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setAdapter(adapter);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            ExpenseUiState s = viewModel.getUiState().getValue();
            if (s == null || s.getCategories() == null || s.getCategories().isEmpty()) {
                viewModel.loadData();
            }
            showTransactionDialog(null);
        });

        btnLoadMore.setOnClickListener(v -> viewModel.loadTransactions(true));
    }

    private void observeUiState() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            tvBudget.setText("Бюджет: " + state.getBudget() + " ₽");
            tvMonthExpense.setText("Расходы за месяц: " + state.getAmountByMonth() + " ₽");
            adapter.setTransactions(state.getTransactions() != null ? state.getTransactions() : new ArrayList<>());
        });

        viewModel.getErrorEvent().observe(getViewLifecycleOwner(), errorEvent -> {
            if (errorEvent != null) {
                ErrorDialog dialog = ErrorDialog.newInstance(
                        errorEvent.getMessage(),
                        errorEvent.getCode()
                );
                dialog.show(getParentFragmentManager(), "error_dialog");
            }
        });
    }

    private void openTransactionDialog(@Nullable Transaction clickedTransaction) {
        Transaction transactionToEdit = null;

        if (clickedTransaction != null) {
            ExpenseUiState state = viewModel.getUiState().getValue();
            if (state != null && state.getTransactions() != null) {
                for (Transaction t : state.getTransactions()) {
                    if (t.getId() == clickedTransaction.getId()) {
                        transactionToEdit = t;
                        break;
                    }
                }
            }
            if (transactionToEdit == null) {
                transactionToEdit = clickedTransaction;
            }
        }

        showTransactionDialog(transactionToEdit);
    }

    private void showTransactionDialog(@Nullable Transaction transaction) {
        ExpenseUiState state = viewModel.getUiState().getValue();
        List<Category> categories = state != null && state.getCategories() != null
                ? state.getCategories()
                : new ArrayList<>();

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_transaction, null);

        TextInputEditText etDate = dialogView.findViewById(R.id.etDate);
        TextInputEditText etAmount = dialogView.findViewById(R.id.etAmount);
        MaterialAutoCompleteTextView etCategory = dialogView.findViewById(R.id.etCategory);

        final ArrayList<String> categoryNames = new ArrayList<>();
        for (Category c : categories) categoryNames.add(c.getName());

        final ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryNames
        );
        etCategory.setAdapter(adapterCategories);

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        if (transaction != null) {
            Date date = transaction.getDateTime();
            if (date != null) etDate.setText(sdf.format(date));
            etAmount.setText(String.valueOf(transaction.getAmount()));
            if (transaction.getCategory() != null) {
                etCategory.setText(transaction.getCategory().getName(), false);
            }
        } else {
            etDate.setText(sdf.format(calendar.getTime()));
        }

        if (categoryNames.isEmpty()) {
            viewModel.loadData();
            viewModel.getUiState().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<ExpenseUiState>() {
                @Override
                public void onChanged(ExpenseUiState newState) {
                    if (newState == null) return;
                    List<Category> newCats = newState.getCategories();
                    if (newCats != null && !newCats.isEmpty()) {
                        categoryNames.clear();
                        for (Category c : newCats) categoryNames.add(c.getName());
                        adapterCategories.notifyDataSetChanged();

                        if (transaction != null && transaction.getCategory() != null) {
                            etCategory.setText(transaction.getCategory().getName(), false);
                        }

                        viewModel.getUiState().removeObserver(this);
                    }
                }
            });
        }

        etDate.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        etDate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(transaction != null ? "Редактировать транзакцию" : "Новая транзакция")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    try {
                        String dateStr = etDate.getText() != null ? etDate.getText().toString().trim() : "";
                        String amountStr = etAmount.getText() != null ? etAmount.getText().toString().trim() : "";
                        String categoryName = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";

                        if (dateStr.isEmpty() || amountStr.isEmpty() || categoryName.isEmpty()) {
                            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int amount = Integer.parseInt(amountStr);

                        List<Category> currentCats = viewModel.getUiState().getValue() != null
                                ? viewModel.getUiState().getValue().getCategories()
                                : new ArrayList<>();
                        Category selectedCategory = null;
                        for (Category c : currentCats) {
                            if (c.getName().equals(categoryName)) {
                                selectedCategory = c;
                                break;
                            }
                        }
                        if (selectedCategory == null) {
                            Toast.makeText(getContext(), "Выберите категорию из списка", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Date date = sdf.parse(dateStr);
                        if (date == null) {
                            Toast.makeText(getContext(), "Неверная дата", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (transaction != null) {
                            Transaction updated = new Transaction(
                                    transaction.getId(),
                                    date,
                                    TRANSACTION_TYPE,
                                    selectedCategory,
                                    amount
                            );
                            viewModel.updateTransaction(updated);
                        } else {
                            Transaction newTransaction = new Transaction(date, TRANSACTION_TYPE, selectedCategory, amount);
                            viewModel.addTransaction(newTransaction);
                        }
                    } catch (NumberFormatException ex) {
                        Toast.makeText(getContext(), "Неверная сумма", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null);

        if (transaction != null) {
            builder.setNeutralButton("Удалить", (dialog, which) -> viewModel.deleteTransaction(transaction));
        }

        builder.show();
    }

}
