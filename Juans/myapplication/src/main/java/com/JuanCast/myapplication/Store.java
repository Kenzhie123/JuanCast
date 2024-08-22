package com.JuanCast.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Store extends AppCompatActivity {

    private Button btnConvert;
    private EditText etSuns;
    private TextView tvEquivalentSuns;
    private TextView tvAvailableSuns;
    private TextView tvAvailableStars;
    private String currentUserId;
    private FirebaseUser user;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;


    private ImageView logo;

    //navvar
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView home;


    private TextView tvSuns;
    private TextView ads;
    private TextView ups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        btnConvert = findViewById(R.id.btnConvert);
        etSuns = findViewById(R.id.etStars);
        tvEquivalentSuns = findViewById(R.id.tvEquivalentSuns);
        tvAvailableSuns = findViewById(R.id.tvAvailableSuns);
        tvAvailableStars = findViewById(R.id.tvAvailableStars);

        ImageView logo = findViewById(R.id.logo);

        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance
        firebaseFirestore = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore instance



        user = auth.getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
            loadStarPoints(currentUserId); // Load user's star points
            loadSunPoints(currentUserId); // Load user's sun points
        } else {
            Log.e(TAG, "User not authenticated.");
        }



        TextView tvSuns = findViewById(R.id.tvStars);
        tvSuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ads = findViewById(R.id.tvAds);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, ads.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ups = findViewById(R.id.tvPowerUps);
        ups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Powerups.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });



        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sunsInput = etSuns.getText().toString().trim(); // Trim to remove leading/trailing spaces
                if (sunsInput.isEmpty()) {
                    Toast.makeText(Store.this, "Please enter suns", Toast.LENGTH_SHORT).show();
                    return;
                }

                int sunsToConvert;
                try {
                    sunsToConvert = Integer.parseInt(sunsInput);
                } catch (NumberFormatException e) {
                    showCustomToast("Invalid input. Please enter a valid number.", R.drawable.juanscast);
                    return;
                }

                // Each sun requires 200 stars
                int requiredStars = sunsToConvert * 200;

                // Retrieve available stars and suns from TextViews
                int availableStars, availableSuns;
                try {
                    availableStars = Integer.parseInt(tvAvailableStars.getText().toString());
                    availableSuns = Integer.parseInt(tvAvailableSuns.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error parsing available stars or suns.", e);
                    showCustomToast("Error retrieving available points. Please try again.", R.drawable.juanscast);
                    return;
                }

                if (requiredStars <= availableStars) {
                    int updatedSuns = availableSuns + sunsToConvert;
                    int remainingStars = availableStars - requiredStars;

                    // Update UI
                    tvEquivalentSuns.setText(""+requiredStars);
                    tvAvailableStars.setText(String.valueOf(remainingStars));
                    tvAvailableSuns.setText(String.valueOf(updatedSuns));

                    // Update Firestore
                    updateStarsAndSuns(currentUserId, remainingStars, updatedSuns);
                } else {
                    showCustomToast("Not enough stars available.", R.drawable.juanscast);
                }
            }
        });


        etSuns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                String sunsInput = s.toString().trim(); // Get the updated input
                if (sunsInput.isEmpty()) {
                    tvEquivalentSuns.setText(""); // Clear equivalent suns if input is empty
                    return;
                }

                int sunsToConvert;
                try {
                    sunsToConvert = Integer.parseInt(sunsInput);
                } catch (NumberFormatException e) {
                    tvEquivalentSuns.setText("Invalid input");
                    return;
                }

                // Calculate required stars
                int requiredStars = sunsToConvert * 200;

                // Retrieve available stars from TextViews
                int availableStars;
                try {
                    availableStars = Integer.parseInt(tvAvailableStars.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error parsing available stars.", e);
                    tvEquivalentSuns.setText("Error retrieving stars");
                    return;
                }

                if (requiredStars <= availableStars) {
                    // Update equivalent suns TextView
                    tvEquivalentSuns.setText(String.valueOf(requiredStars));
                    tvEquivalentSuns.setTextColor(Color.BLACK); // Reset color to default
                } else {
                    tvEquivalentSuns.setText("Not enough stars");
                    tvEquivalentSuns.setTextColor(Color.RED); // Reset color to default
                }
            }
        });



        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Homepage.class); // Replace CurrentActivity with your current activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        //navvar
        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

    }

    private void refreshPoints(String userId) {
        loadStarPoints(userId); // Reload star points
        loadSunPoints(userId); // Reload sun points
    }

    private void showCustomToast(String message, int imageResId) {
        // Gumawa ng LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();

        // Inflate ng custom layout
        View layout = inflater.inflate(R.layout.imagetoast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        // I-set ang larawan at teksto sa custom layout
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageResource(imageResId); // I-set ang larawan batay sa image resource ID na ibinigay

        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(message);

        // Lumikha ng Toast object
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void updateStarsAndSuns(String userId, int remainingStars, int updatedSuns) {
        firebaseFirestore.collection("User").document(userId)
                .update("votingPoints", remainingStars, "sunvotingpoints", updatedSuns)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Stars and Suns updated successfully");
                        showCustomToast("Points updated successfully", R.drawable.juanscast);
                        autoRefreshPoints(userId); // Auto refresh points after update
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating stars and suns", e);
                        showCustomToast("Failed to update points", R.drawable.juanscast);
                    }
                });
    }


    private void loadStarPoints(String userId) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long votingPoints = documentSnapshot.getLong("votingPoints");
                        if (votingPoints != null) {
                            tvAvailableStars.setText(String.valueOf(votingPoints));
                        } else {
                            tvAvailableStars.setText("No voting points available");
                        }
                    } else {
                        tvAvailableStars.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    tvAvailableStars.setText("Failed to load voting points");
                    Log.e(TAG, "Error fetching voting points: " + e.getMessage());
                });
    }

    private void loadSunPoints(String userId) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long sunVotingPoints = documentSnapshot.getLong("sunvotingpoints");
                        if (sunVotingPoints != null) {
                            tvAvailableSuns.setText(String.valueOf(sunVotingPoints));
                        } else {
                            tvAvailableSuns.setText("No sun voting points available");
                        }
                    } else {
                        tvAvailableSuns.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    tvAvailableSuns.setText("Failed to load sun voting points");
                    Log.e(TAG, "Error fetching sun voting points: " + e.getMessage());
                });
    }



    private void autoRefreshPoints(String userId) {
        loadStarPoints(userId); // Reload star points
        loadSunPoints(userId); // Reload sun points
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            autoRefreshPoints(currentUserId);
        }
    }

}
