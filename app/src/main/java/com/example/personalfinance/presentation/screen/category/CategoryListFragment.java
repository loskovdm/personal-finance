package com.example.personalfinance.presentation.screen.category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalfinance.R;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.presentation.model.CategoryUiModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryListFragment extends Fragment {

    protected CategoryViewModel viewModel;
    protected CategoryAdapter adapter;
    protected TransactionType type;

    private RecyclerView rvCategories;
    private FloatingActionButton fabAdd;

    public CategoryListFragment() {}

    public static CategoryListFragment newInstance(TransactionType type) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString("type", type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String typeName = getArguments().getString("type");
            type = TransactionType.valueOf(typeName);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        initViews(view);
        setupRecycler();
        setupListeners();
        observeState();

        viewModel.setType(type);
    }

    protected void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        fabAdd = view.findViewById(R.id.fabAddCategory);
    }

    protected void setupRecycler() {
        adapter = new CategoryAdapter(this::showEditCategoryDialog);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategories.setAdapter(adapter);
    }

    protected void setupListeners() {
        fabAdd.setOnClickListener(v -> showAddCategoryDialog());
    }

    protected void observeState() {
        viewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            if (state.getErrorMessage() != null) {
                Toast.makeText(getContext(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }

            adapter.submitList(state.getCategories());
        });
    }

    protected void showAddCategoryDialog() {
        android.view.View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_edit_category, null);

        com.google.android.material.textfield.TextInputEditText etName =
                dialogView.findViewById(R.id.etText);

        new AlertDialog.Builder(getContext())
                .setTitle("Новая категория")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        viewModel.addCategory(name);
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    protected void showEditCategoryDialog(CategoryUiModel category) {
        android.view.View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_edit_category, null);

        com.google.android.material.textfield.TextInputEditText etName =
                dialogView.findViewById(R.id.etText);
        etName.setText(category.getName());

        new AlertDialog.Builder(getContext())
                .setTitle("Редактировать категорию")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        CategoryUiModel updated = new CategoryUiModel(
                                category.getId(),
                                newName
                        );
                        viewModel.updateCategory(updated);
                    }
                })
                .setNeutralButton("Удалить", (dialog, which) -> {
                    viewModel.deleteCategory(category);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}