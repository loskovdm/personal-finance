package com.example.personalfinance.presentation.screen.analytics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalfinance.R;import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsFragment extends Fragment {

    private TabLayout tabLayout;
    private androidx.viewpager2.widget.ViewPager2 viewPager;
    private AnalyticsPagerAdapter adapter;

    private AnalyticsViewModel analyticsViewModel;

    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;
    private MaterialButton btnApplyPeriod;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private Date currentStartDate;
    private Date currentEndDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        analyticsViewModel = new ViewModelProvider(requireActivity()).get(AnalyticsViewModel.class);

        initViews(view);
        setupViewPager();
        setupDatePickers();
        observeViewModel();
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnApplyPeriod = view.findViewById(R.id.btnApplyPeriod);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        currentStartDate = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        currentEndDate = cal.getTime();
    }

    private void setupViewPager() {
        adapter = new AnalyticsPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Общая");
                    break;
                case 1:
                    tab.setText("Расходы");
                    break;
                case 2:
                    tab.setText("Доходы");
                    break;
            }
        }).attach();
    }

    private void setupDatePickers() {
        etStartDate.setOnClickListener(v -> showDatePicker(true));

        etEndDate.setOnClickListener(v -> showDatePicker(false));

        btnApplyPeriod.setOnClickListener(v -> applyPeriod());
    }

    private void applyPeriod() {
        try {
            String startDateStr = etStartDate.getText().toString();
            String endDateStr = etEndDate.getText().toString();

            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate != null && endDate != null) {
                AnalyticsSharedViewModel sharedViewModel =
                        new ViewModelProvider(requireActivity()).get(AnalyticsSharedViewModel.class);
                sharedViewModel.updatePeriod(startDate, endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void observeViewModel() {
        analyticsViewModel.getSelectedPeriod().observe(getViewLifecycleOwner(), period -> {
            if (period != null) {
                etStartDate.setText(dateFormat.format(period.getStartDate()));
                etEndDate.setText(dateFormat.format(period.getEndDate()));

                currentStartDate = period.getStartDate();
                currentEndDate = period.getEndDate();
            }
        });
    }

    private void showDatePicker(final boolean isStartDate) {
        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    Date selectedDate = calendar.getTime();

                    if (isStartDate) {
                        currentStartDate = selectedDate;
                        etStartDate.setText(dateFormat.format(selectedDate));
                    } else {
                        currentEndDate = selectedDate;
                        etEndDate.setText(dateFormat.format(selectedDate));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }
}