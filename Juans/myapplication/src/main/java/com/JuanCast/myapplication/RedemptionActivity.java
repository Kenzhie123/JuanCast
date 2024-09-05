package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.google.firebase.auth.FirebaseAuth;

public class RedemptionActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RedemptionAdapter adapter;
    private RecyclerView recyclerView;

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

        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);

        ads = findViewById(R.id.ads);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        cast = findViewById(R.id.cast);

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        ads.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, RewardActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        t_purchasebutton.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, PurchaseTransactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        cast.setOnClickListener(v -> {
            Intent intent = new Intent(RedemptionActivity.this, Transactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Fetch current user ID
        String currentUserId = auth.getCurrentUser().getUid();

        // Query Firestore to get the latest redemptions for the current user
        CollectionReference redemptionRef = firebaseFirestore.collection("promoCodeRedemptions");
        Query query = redemptionRef.whereEqualTo("userId", currentUserId)
                .orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Redemption> options = new FirestoreRecyclerOptions.Builder<Redemption>()
                .setQuery(query, Redemption.class)
                .build();

        adapter = new RedemptionAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
