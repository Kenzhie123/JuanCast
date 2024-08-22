package com.JuanCast.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.dateTextView.setText("Date: " + transaction.getDate());
        holder.referenceNumberTextView.setText("Reference Number: " + transaction.getReferenceNumber());
        holder.starTextView.setText("Star: " + transaction.getStar());
        holder.sunTextView.setText("Sun: " + transaction.getSun());
        holder.timeTextView.setText("Time: " + transaction.getTime());
        holder.transactionTypeTextView.setText("Transaction Type: " + transaction.getTransactionType());
        holder.userIdTextView.setText("User ID: " + transaction.getUserId());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView referenceNumberTextView;
        TextView starTextView;
        TextView sunTextView;
        TextView timeTextView;
        TextView transactionTypeTextView;
        TextView userIdTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.item_date);
            referenceNumberTextView = itemView.findViewById(R.id.item_reference_number);
            starTextView = itemView.findViewById(R.id.item_star);
            sunTextView = itemView.findViewById(R.id.item_sun);
            timeTextView = itemView.findViewById(R.id.item_time);
            transactionTypeTextView = itemView.findViewById(R.id.item_transaction_type);
            userIdTextView = itemView.findViewById(R.id.item_user_id);

        }
    }
}
