package com.example.personalfinance.domain.model;

import java.util.Date;
import java.util.List;

public class ReportRequest {
    private final TransactionType type;
    private final Date startDate;
    private final Date finishDate;
    private final Boolean isCategorization;
    private final List<Category> includedCategory;

    public ReportRequest(
            TransactionType type,
            Date startDate,
            Date finishDate,
            Boolean isCategorization,
            List<Category> includedCategory
    ) {
        this.type = type;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isCategorization = isCategorization;
        this.includedCategory = includedCategory;
    }

    public TransactionType getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public Boolean getCategorization() {
        return isCategorization;
    }

    public List<Category> getIncludedCategory() {
        return includedCategory;
    }
}
