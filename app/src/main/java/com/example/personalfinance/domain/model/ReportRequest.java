package com.example.personalfinance.domain.model;

import java.util.Date;
import java.util.List;

public class ReportRequest {

    /** Transaction type filter.
     * If one type is set, only that type is included.
     * If both types are set, a general (non-categorized) report is returned.
     */
    private final TransactionType type;

    /** Start date of the report.
     * If null, transactions are counted from the earliest date.
     */
    private final Date startDate;

    /** End date of the report.
     * If null, transactions are counted up to the latest date.
     */
    private final Date finishDate;

    /** Whether to include category breakdown.
     * true → show per-category values,
     * false → show only total amount.
     */
    private final Boolean isCategorization;

    /** Optional category filter.
     * Only transactions from these categories are included.
     * If null → no filtering.
     */
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
