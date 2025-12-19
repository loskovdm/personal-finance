package com.example.personalfinance.presentation.screen.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalfinance.R;
import com.example.personalfinance.domain.model.CategoryValue;

import java.util.List;

public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.ViewHolder> {

    private List<CategoryValue> categoryValues;

    public void setCategoryValues(List<CategoryValue> categoryValues) {
        this.categoryValues = categoryValues;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_analytics_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        CategoryValue categoryValue = categoryValues.get(position);

//        holder.tvCategoryName.setText(categoryValue.getCategory().getName());
//        holder.tvCategoryAmount.setText(String.format("%d ₽", categoryValue.getAmount()));
//
//        // int total = calculateTotal();
//        // float percentage = (categoryValue.getAmount() * 100f) / total;
//        // holder.tvCategoryPercentage.setText(String.format("%.1f%%", percentage));
    }

    @Override
    public int getItemCount() {
        return categoryValues != null ? categoryValues.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        TextView tvCategoryAmount;
        TextView tvCategoryPercentage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryAmount = itemView.findViewById(R.id.tvCategoryAmount);
            tvCategoryPercentage = itemView.findViewById(R.id.tvCategoryPercentage);
        }
    }
}