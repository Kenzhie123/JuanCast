package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RewardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RewardAdapter adapter;
    private List<Reward> rewardList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView home;
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView profile;


    private TextView cast;
    private TextView t_purchasebutton;
    private TextView promo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rewardList = new ArrayList<>();
        adapter = new RewardAdapter(rewardList);
        recyclerView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        home = findViewById(R.id.home);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);
        profile = findViewById(R.id.profile);

        cast = findViewById(R.id.cast);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        promo = findViewById(R.id.promo);

        swipeRefreshLayout.setOnRefreshListener(() -> loadRewards());

        // Trigger the refresh programmatically
        swipeRefreshLayout.setRefreshing(true);
        loadRewards();

        // Navigation variables
        Community.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        cast.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, Transactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        t_purchasebutton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, PurchaseTransactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        promo.setOnClickListener(v -> {
            Intent intent = new Intent(RewardActivity.this, RedemptionActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }

    private void loadRewards() {
        String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("RewardActivity", "User ID is null. User may not be authenticated.");
            swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
            return;
        }

        Log.d("RewardActivity", "Fetching rewards for user ID: " + userId);

        firebaseFirestore.collection("AdViews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Reward> newRewards = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("RewardActivity", "Document ID: " + document.getId());
                            Reward reward = document.toObject(Reward.class);
                            if (reward != null) {
                                newRewards.add(reward);
                            } else {
                                Log.w("RewardActivity", "Reward is null for document ID: " + document.getId());
                            }
                        }

                        Log.d("RewardActivity", "New rewards fetched: " + newRewards.size());

                        // Prepend new rewards to the start of the list
                        rewardList.addAll(0, newRewards);  // Insert new rewards at the beginning
                        adapter.notifyDataSetChanged();    // Notify the adapter of data changes

                        Log.d("RewardActivity", "Rewards successfully loaded. Total: " + rewardList.size());
                    } else {
                        Log.e("RewardActivity", "Error getting documents: ", task.getException());
                    }

                    // Stop the refreshing animation
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
}
