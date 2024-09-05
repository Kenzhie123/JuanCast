package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private FirebaseAuth firebaseAuth;  // Add FirebaseAuth instance

    private ImageView logo;



    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;

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
        firebaseAuth = FirebaseAuth.getInstance();  // Initialize FirebaseAuth


        home = findViewById(R.id.home);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);

        cast = findViewById(R.id.cast);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        promo = findViewById(R.id.promo);



        //navvar

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardActivity.this, Transactions.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
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

        loadRewards();
    }

    private void loadRewards() {
        String userId = firebaseAuth.getCurrentUser().getUid();  // Get current user ID

        if (userId == null) {
            Log.e("RewardActivity", "User ID is null. User may not be authenticated.");
            return;
        }

        Log.d("RewardActivity", "Fetching rewards for user ID: " + userId);

        firebaseFirestore.collection("AdViews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Reward> newRewards = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("RewardActivity", "Document ID: " + document.getId());
                                Reward reward = document.toObject(Reward.class);
                                newRewards.add(reward);
                            }

                            // Check the contents of newRewards
                            Log.d("RewardActivity", "New rewards fetched: " + newRewards.size());

                            // Prepend new rewards to the start of the list
                            rewardList.addAll(0, newRewards);  // Insert new rewards at the beginning
                            adapter.notifyDataSetChanged();    // Notify the adapter of data changes

                            Log.d("RewardActivity", "Rewards successfully loaded. Total: " + rewardList.size());
                        } else {
                            Log.d("RewardActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }




}
