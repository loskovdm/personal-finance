package com.example.personalfinance.presentation.screen.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalfinance.R;
import com.example.personalfinance.domain.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<Transaction> transactions = new ArrayList<>();
    private final OnTransactionClickListener listener;

    public TransactionAdapter(List<Transaction> initialTransactions, OnTransactionClickListener listener) {
        if (initialTransactions != null) {
            this.transactions.addAll(initialTransactions);
        }
        this.listener = listener;
    }

    public void setTransactions(List<Transaction> newTransactions) {
        if (newTransactions == null) newTransactions = new ArrayList<>();
        final List<Transaction> finalNewTransactions = newTransactions;

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return transactions.size();
            }

            @Override
            public int getNewListSize() {
                return finalNewTransactions.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return transactions.get(oldItemPosition).getId() ==
                        finalNewTransactions.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return transactions.get(oldItemPosition).equals(finalNewTransactions.get(newItemPosition));
            }
        });

        transactions.clear();
        transactions.addAll(finalNewTransactions);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAmount;
        private final TextView tvCategory;
        private final TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvCategory = itemView.findViewById(R.id.tv_transaction_category);
            tvDate = itemView.findViewById(R.id.tv_transaction_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onTransactionClick(transactions.get(pos));
                }
            });
        }

        public void bind(Transaction transaction) {
            tvAmount.setText(transaction.getAmount() + " ₽");
            tvCategory.setText(transaction.getCategory().getName());
            tvDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(transaction.getDateTime()));
        }
    }

    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }
}
