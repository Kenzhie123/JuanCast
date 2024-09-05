package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Transactions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ImageView logo;

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    private TextView ads;
    private TextView t_purchasebutton;
    private TextView promo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(transactionAdapter);


        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);


        ads = findViewById(R.id.ads);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        promo = findViewById(R.id.promo);

        fetchDataFromFirestore();

        //navvar

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });



        ads.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, RewardActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        t_purchasebutton.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, PurchaseTransactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        promo.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions.this, RedemptionActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }



    private void fetchDataFromFirestore() {
        // Get the current user ID
        String userId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("transaction_history")
                .whereEqualTo("user_id",userId)
                .whereEqualTo("transaction_type","vote")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Transaction> newTransactions = new ArrayList<>();
                        Log.d("TESTTAG",task.getResult().size() + "Size");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Manually extract fields
                            String date = document.getString("date");
                            String referenceNumber = document.getString("reference_number");
                            int star = document.getLong("star").intValue();
                            int sun = document.getLong("sun").intValue();
                            String time = document.getString("time");
                            String transactionType = document.getString("transaction_type");
                            Timestamp timestamp = document.getTimestamp("timestamp");  // Fetching timestamp

                            // Create Transaction object with timestamp
                            Transaction transaction = new Transaction(date, referenceNumber, star, sun, time, transactionType, userId, timestamp);
                            newTransactions.add(transaction);
                        }

                        // Debug logging for fetched transactions
                        Log.d("Firestore", "Fetched new transactions: " + newTransactions.toString());

                        // Sort newTransactions by timestamp in descending order
                        Collections.sort(newTransactions, (t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));

                        // Prepend new transactions to the start of the list
                        transactionList.addAll(0, newTransactions);

                        // Debug logging for updated transaction list
                        Log.d("Firestore", "Updated transaction list: " + transactionList.toString());

                        // Notify the adapter about data changes
                        transactionAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

    }
    }
