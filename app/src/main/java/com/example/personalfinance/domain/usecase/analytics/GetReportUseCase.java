package com.example.personalfinance.domain.usecase.analytics;

import com.example.personalfinance.domain.model.Category;
import com.example.personalfinance.domain.model.ReportRequest;
import com.example.personalfinance.domain.model.ReportResponse;
import com.example.personalfinance.domain.model.TransactionType;
import com.example.personalfinance.domain.repository.TransactionRepository;

import java.util.Date;
import java.util.List;

public class GetReportUseCase {
    private final TransactionRepository repository;

    public GetReportUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public ReportResponse execute(
            TransactionType type,
            Date startDate,
            Date finishDate,
            List<Category> includedCategory
    ) {
        ReportRequest request = new ReportRequest(
                type,
                startDate,
                finishDate,
                true,
                includedCategory
        );
        return repository.getTransactionReport(request);
    }
}
