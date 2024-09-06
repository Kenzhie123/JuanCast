package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Powerups extends AppCompatActivity {

    // Navigation variables
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView home;
    private ImageView logo;

    // TextView variables
    private TextView tvSuns;
    private TextView ads;
    private TextView star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerups);

        // Initialize ImageViews
        Community = findViewById(R.id.Community);
        profile = findViewById(R.id.profile);
        Cast = findViewById(R.id.Cast);
        home = findViewById(R.id.home);
        logo = findViewById(R.id.logo); // Make sure to set the correct ID for logo

        // Initialize TextViews
        tvSuns = findViewById(R.id.tvSuns);
        ads = findViewById(R.id.tvAds);
        star = findViewById(R.id.tvStars);

        // Set OnClickListeners for ImageViews
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                overridePendingTransition(0, 0); // No animation
                startActivity(intent);
                finish();
            }
        });

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        // Set OnClickListeners for TextViews
        tvSuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Store.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, ads.class); // Ensure the class name is correctly capitalized
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });
    }
}
