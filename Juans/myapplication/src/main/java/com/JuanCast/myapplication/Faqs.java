package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Faqs extends AppCompatActivity {

    private ImageView back;

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        List<FAQItem> faqList = new ArrayList<>();
        faqList.add(new FAQItem("How do I use this app?", "You can start by opening the app and following the \n tutorial."));
        faqList.add(new FAQItem("What is the refund policy?", "Please refer to our refund policy in the settings."));

        FAQAdapter adapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(adapter);


        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);


        back.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });



        //navvar

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Faqs.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
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