package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Notices extends AppCompatActivity {

    private ImageView back;
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private List<AnnouncementList> AnnounceList; // Assuming Post is the correct type
    private AnnouncementAdapter announcementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        // Initialize views
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);

        // Initialize Firebase Firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        AnnounceList = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(AnnounceList); // Ensure class name is correct
        recyclerView.setAdapter(announcementAdapter);

        // Load posts
        loadPosts();

        // Set up navigation buttons
        setUpNavigation();
    }

    private void setUpNavigation() {
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Notices.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }

    private void loadPosts() {
        firebaseFirestore.collection("announcements")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AnnounceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AnnouncementList AnnouncementList  = document.toObject(AnnouncementList.class);
                            AnnounceList.add(AnnouncementList);
                        }
                        announcementAdapter.notifyDataSetChanged();
                    } else {
                        // Handle errors here
                        Log.e("Notices", "Error getting documents: ", task.getException());
                    }
                    // Hide the refreshing indicator when done
                });
    }

    @Override
    public void onBackPressed() {
        // Perform custom action before calling the default behavior
        Intent intent = new Intent(this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Use overridePendingTransition after startActivity
        overridePendingTransition(0, 0);

        // Call the default back behavior
        super.onBackPressed();
    }
}
