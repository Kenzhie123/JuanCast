package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotifButton extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<AnnouncementList> announcementList;
    private FirebaseFirestore firebaseFirestore;

    //navvar
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView Store;
    private ImageView Home;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_button);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter(announcementList);
        recyclerView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        fetchAnnouncements();

        //navvar

        Home = findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifButton.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifButton.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        Store = findViewById(R.id.Store);
        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifButton.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifButton.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifButton.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });




    }

    @Override
    public void onBackPressed() {
        // Perform custom action before calling the default behavior
        Intent intent = new Intent(this, Homepage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Use overridePendingTransition after startActivity
        overridePendingTransition(0, 0);

        // Call the default back behavior
        super.onBackPressed();
    }



    private void fetchAnnouncements() {
        firebaseFirestore.collection("announcements")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            announcementList.clear();
                            List<AnnouncementList> announcements = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AnnouncementList announcement = document.toObject(AnnouncementList.class);
                                announcements.add(announcement);
                            }

                            announcementList.addAll(announcements);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(NotifButton.this, "Error getting announcements", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}