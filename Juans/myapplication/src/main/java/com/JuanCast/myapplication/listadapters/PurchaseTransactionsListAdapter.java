package com.JuanCast.myapplication.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.JuanCast.myapplication.R;
import com.JuanCast.myapplication.Tools;
import com.JuanCast.myapplication.models.PurchaseTransaction;

import java.util.ArrayList;

public class PurchaseTransactionsListAdapter extends RecyclerView.Adapter<PurchaseTransactionsListAdapter.PurchaseTransactionListViewHolder> {

    Context context;
    ArrayList<PurchaseTransaction> purchaseTransactionList;
    public PurchaseTransactionsListAdapter(Context context, ArrayList<PurchaseTransaction> purchaseTransactionList)
    {
        this.context = context;
        this.purchaseTransactionList = purchaseTransactionList;

    }

    @NonNull
    @Override
    public PurchaseTransactionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchaseTransactionListViewHolder(LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.purchase_transaction_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseTransactionListViewHolder holder, int position) {
        PurchaseTransaction currentPurchaseTransaction = purchaseTransactionList.get(position);
        String dateText = "Date: "+Tools.dateToString(currentPurchaseTransaction.getTimeStamp().toDate());
        String timeText = "Time: "+Tools.timeToString(currentPurchaseTransaction.getTimeStamp().toDate());
        String starText = "Star: "+currentPurchaseTransaction.getStarAmount();
        String transactionIDText = "Transaction ID: "+currentPurchaseTransaction.getTransactionID();
        String amountChargedText = "Amount Charged: "+currentPurchaseTransaction.getAmountCharged();
        holder.TI_Date.setText(dateText);
        holder.TI_Time.setText(timeText);
        holder.TI_Star.setText(starText);
        holder.TI_TransactionID.setText(transactionIDText);
        holder.TI_AmountCharged.setText(amountChargedText);
    }

    @Override
    public int getItemCount() {
        return purchaseTransactionList.size();
    }

    public class PurchaseTransactionListViewHolder extends RecyclerView.ViewHolder{

        TextView TI_Date;
        TextView TI_Time;
        TextView TI_Star;
        TextView TI_TransactionID;
        TextView TI_AmountCharged;
        public PurchaseTransactionListViewHolder(@NonNull View itemView) {
            super(itemView);
            TI_Date = itemView.findViewById(R.id.TI_Date);
            TI_Time = itemView.findViewById(R.id.TI_Time);
            TI_Star = itemView.findViewById(R.id.TI_Star);
            TI_TransactionID = itemView.findViewById(R.id.TI_TransactionID);
            TI_AmountCharged = itemView.findViewById(R.id.TI_AmountCharged);
        }
    }
}
