package com.example.personalfinance.domain.usecase.budget;

import com.example.personalfinance.domain.exception.ValidationException;
import com.example.personalfinance.domain.repository.BudgetRepository;

import java.util.Date;

public class GetBudgetFotPeriodUseCase {
    private final BudgetRepository repository;

    public GetBudgetFotPeriodUseCase(BudgetRepository repository) {
        this.repository = repository;
    }

    public int execute(Date startDate, Date finishDate) {
        validate(startDate, finishDate);
        return repository.getBudgetForPeriod(startDate, finishDate);
    }

    private void validate(Date startDate, Date finishDate) {
        if (startDate.after(finishDate)) {
            throw new ValidationException("The start date must be before the finish date");
        }
    }
}
