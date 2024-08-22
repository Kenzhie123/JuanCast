package com.JuanCast.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.JuanCast.myapplication.Receiver.StarChangeReceiver;
import com.JuanCast.myapplication.Receiver.VotingChangeReceiver;
import com.JuanCast.myapplication.listadapters.PollListAdapter;
import com.JuanCast.myapplication.models.Poll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Voting extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private int votingPoints;
    private String username;
    private DocumentReference userDocRef; // Document reference for the user document in Firestore

    private SwipeRefreshLayout V_PollRefreshLayout;
    private RecyclerView V_PollRecyclerView;
    private PollListAdapter pollListAdapter;
    private ArrayList<Poll> pollList;

    // Navigation variables
    private ImageView Community;
    private ImageView Store;
    private ImageView Storetop;
    private ImageView Profile;
    private ImageView home;
    private ImageView logo;

    private RelativeLayout noInternetLayout;
    private VotingChangeReceiver networkChangeReceiver;

    // Getter para sa noInternetLayout
    public RelativeLayout getNoInternetLayout() {
        return noInternetLayout;
    }

    public void initPollList() {
        pollList = new ArrayList<>();
        firebaseFirestore.collection("voting_polls").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        if (((String) document.getData().get("visibility")).equals("visible")) {
                            Poll poll = new Poll(
                                    document.getId(),
                                    (String) document.getData().get("poll_title"),
                                    Tools.StringToDate((String) document.getData().get("date_from")),
                                    Tools.StringToDate((String) document.getData().get("date_to")),
                                    Tools.StringToTime((String) document.getData().get("time_end")),
                                    (String) document.getData().get("note"),
                                    (ArrayList<String>) document.getData().get("artists"),
                                    (ArrayList<String>) document.getData().get("tag_list"),
                                    (String) document.getData().get("poll_type"),
                                    (String) document.getData().get("visibility")
                            );
                            pollList.add(poll);
                        }
                    }
                    V_PollRefreshLayout.setRefreshing(false);
                    pollListAdapter = new PollListAdapter(getApplicationContext(), pollList);
                    V_PollRecyclerView.setAdapter(pollListAdapter);
                    V_PollRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        // Initialize Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        V_PollRefreshLayout = findViewById(R.id.V_PollRefreshLayout);
        V_PollRecyclerView = findViewById(R.id.V_PollRecyclerView);

        // Initialize navigation ImageViews
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Storetop = findViewById(R.id.notification_icon);
        Profile = findViewById(R.id.profile);
        home = findViewById(R.id.home);
        logo = findViewById(R.id.logo);

        // Set up the navigation click listeners
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, Homepage.class); // Replace Homepage.class with your actual homepage class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                overridePendingTransition(0, 0); // No animation
                startActivity(intent);
                finish();
            }
        });

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Storetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voting.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        initPollList();

        V_PollRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPollList();
            }
        });

        noInternetLayout = findViewById(R.id.noInternetLayout);
        networkChangeReceiver = new VotingChangeReceiver();

        // Initial check for internet connectivity
        if (!isConnected()) {
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            noInternetLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register receiver to listen for network changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister receiver to avoid memory leaks
        unregisterReceiver(networkChangeReceiver);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
