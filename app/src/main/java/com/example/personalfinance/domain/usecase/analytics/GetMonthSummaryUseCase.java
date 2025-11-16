package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

public class GetMonthSummaryUseCase {
    private final TransactionRepository repository;

    @Inject
    public GetMonthSummaryUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public int execute(TransactionType type) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date finishDate = calendar.getTime();

        return repository.getTransactionTotalAmountByType(type, startDate, finishDate, null);
    }
}
