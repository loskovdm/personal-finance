package com.example.personalfinance.presentation.screen.analytics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class AnalyticsViewModel extends ViewModel {

    private final MutableLiveData<DatePeriod> selectedPeriod = new MutableLiveData<>();

    @Inject
    public AnalyticsViewModel() {
        setDefaultPeriod();
    }

    public LiveData<DatePeriod> getSelectedPeriod() {
        return selectedPeriod;
    }

    public void updatePeriod(Date startDate, Date endDate) {
        selectedPeriod.setValue(new DatePeriod(startDate, endDate));
    }

    private void setDefaultPeriod() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        calendar.set(java.util.Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        calendar.set(java.util.Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        selectedPeriod.setValue(new DatePeriod(startDate, endDate));
    }

    public static class DatePeriod {
        private final Date startDate;
        private final Date endDate;

        public DatePeriod(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }
    }
}