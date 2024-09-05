package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RedemptionActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RedemptionAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    private TextView ads;
    private TextView t_purchasebutton;
    private TextView cast;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);

        ads = findViewById(R.id.ads);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        cast = findViewById(R.id.cast);

        setupNavigation();
        setupFirestore();

        swipeRefreshLayout.setOnRefreshListener(() -> loadRewards());

        // Start with refreshing
        swipeRefreshLayout.setRefreshing(true);
        loadRewards();
    }

    private void setupNavigation() {
        Community.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, PostActivity.class));
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, StarStore.class));
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, Voting.class));
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, Homepage.class));
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, Profile.class));
            overridePendingTransition(0, 0); // No animation
        });

        ads.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, RewardActivity.class));
            overridePendingTransition(0, 0); // No animation
        });

        t_purchasebutton.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, PurchaseTransactions.class));
            overridePendingTransition(0, 0); // No animation
        });

        cast.setOnClickListener(v -> {
            startActivity(new Intent(RedemptionActivity.this, Transactions.class));
            overridePendingTransition(0, 0); // No animation
        });
    }

    private void setupFirestore() {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            // Handle case where user ID is not available
            return;
        }

        CollectionReference redemptionRef = firebaseFirestore.collection("promoCodeRedemptions");
        Query query = redemptionRef.whereEqualTo("userId", currentUserId)
                .orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Redemption> options = new FirestoreRecyclerOptions.Builder<Redemption>()
                .setQuery(query, Redemption.class)
                .build();

        adapter = new RedemptionAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    private void loadRewards() {
        // Reload data with the current query options
        adapter.notifyDataSetChanged();

        // Stop the refreshing animation after data is loaded
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
