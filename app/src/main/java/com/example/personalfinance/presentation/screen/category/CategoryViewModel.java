package com.example.personalfinance.presentation.screen.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.usecase.category.AddCategoryUseCase;
import com.example.personalfinance.domain.usecase.category.DeleteCategoryUseCase;
import com.example.personalfinance.domain.usecase.category.GetCategoriesByTypeUseCase;
import com.example.personalfinance.domain.usecase.category.UpdateCategoryUseCase;
import com.example.personalfinance.presentation.mapper.UiCategoryMapper;
import com.example.personalfinance.presentation.model.CategoryUiModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoryViewModel extends ViewModel {

    private final GetCategoriesByTypeUseCase getCategoriesByTypeUseCase;
    private final AddCategoryUseCase addCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<CategoryUiState> uiState =
            new MutableLiveData<>(new CategoryUiState());

    public LiveData<CategoryUiState> getUiState() {
        return uiState;
    }

    @Inject
    public CategoryViewModel(
            GetCategoriesByTypeUseCase getCategoriesByTypeUseCase,
            AddCategoryUseCase addCategoryUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.getCategoriesByTypeUseCase = getCategoriesByTypeUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;

        loadCategories();
    }

    public void setType(TransactionType type) {
        CategoryUiState current = uiState.getValue();
        if (current == null) return;

        uiState.setValue(current.copy(
                type,
                current.isLoading(),
                current.getCategories(),
                null
        ));

        loadCategories();
    }

    public void loadCategories() {
        setLoading(true);

        executor.execute(() -> {
            try {
                CategoryUiState current = uiState.getValue();
                if (current == null) return;

                TransactionType type = current.getType();

                List<Category> domainCategories =
                        getCategoriesByTypeUseCase.execute(type);

                List<CategoryUiModel> uiModels = domainCategories.stream()
                        .map(UiCategoryMapper::toUi)
                        .collect(Collectors.toList());

                uiState.postValue(new CategoryUiState(
                        false,
                        type,
                        uiModels,
                        null
                ));

            } catch (Exception e) {
                setError(e.getMessage());
            }
        });
    }

    public void addCategory(String name) {
        executor.execute(() -> {
            try {
                TransactionType type = uiState.getValue().getType();
                Category category = new Category(0, name, type);

                addCategoryUseCase.execute(category);

                loadCategories();

            } catch (Exception e) {
                setError(e.getMessage());
            }
        });
    }

    public void updateCategory(CategoryUiModel uiModel) {
        executor.execute(() -> {
            try {
                TransactionType type = uiState.getValue().getType();
                Category category = UiCategoryMapper.toDomain(uiModel, type);

                updateCategoryUseCase.execute(category);

                loadCategories();

            } catch (Exception e) {
                setError(e.getMessage());
            }
        });
    }

    public void deleteCategory(CategoryUiModel uiModel) {
        executor.execute(() -> {
            try {
                TransactionType type = uiState.getValue().getType();
                Category category = UiCategoryMapper.toDomain(uiModel, type);

                deleteCategoryUseCase.execute(category);

                loadCategories();

            } catch (Exception e) {
                setError(e.getMessage());
            }
        });
    }

    private void setLoading(boolean isLoading) {
        CategoryUiState current = uiState.getValue();
        if (current == null) return;

        uiState.postValue(current.copy(
                current.getType(),
                isLoading,
                current.getCategories(),
                null
        ));
    }

    private void setError(String message) {
        CategoryUiState current = uiState.getValue();
        if (current == null) return;

        uiState.postValue(current.copy(
                current.getType(),
                false,
                current.getCategories(),
                message
        ));
    }

    public void clearError() {
        CategoryUiState current = uiState.getValue();
        if (current == null) return;

        uiState.postValue(current.copy(
                current.getType(),
                current.isLoading(),
                current.getCategories(),
                null
        ));
    }
}
