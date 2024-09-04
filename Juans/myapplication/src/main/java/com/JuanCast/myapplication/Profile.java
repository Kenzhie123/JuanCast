package com.JuanCast.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.Receiver.ProfileChangeReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Button logout;
    private Uri imageUri;

    private ProgressBar profileImageProgressBar;




    private TextView welcomeTextView;
    private TextView fullTextView;
    private TextView dateTextView;


    private TextView starpoints;
    private TextView sunpoints;

    private String currentUserId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ImageView logo;

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView Storetop;
    private TextView transaction;
    private TextView reportIssue;
    private TextView Setting;
    private  TextView Faqs;
    private  TextView Notices;
    private  TextView promo;


    private RelativeLayout noInternetLayout;
    private ProfileChangeReceiver networkChangeReceiver;


    // Getter para sa noInternetLayout
    public RelativeLayout getNoInternetLayout() {
        return noInternetLayout;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reportIssue = findViewById(R.id.reportIssue);
        Setting = findViewById(R.id.Setting);
        Faqs = findViewById(R.id.FAQs);
        Notices = findViewById(R.id.Notices);
        promo = findViewById(R.id.promo);

        transaction = findViewById(R.id.transaction);
        logo = findViewById(R.id.logo);
        starpoints = findViewById(R.id.starpoints);
        sunpoints = findViewById(R.id.sunpoints);

        profileImageProgressBar = findViewById(R.id.profileImageProgressBar);


        welcomeTextView = findViewById(R.id.welcomeTextView);
        fullTextView = findViewById(R.id.fullTextView);
        dateTextView = findViewById(R.id.dateTextView);
        profileImageView = findViewById(R.id.profileImageView);
        Button btnLogout = findViewById(R.id.btnLogout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance
        firebaseFirestore = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore instance



        user = auth.getCurrentUser();
        currentUserId = user.getUid();

        profileImageView.setOnClickListener(v -> openFileChooser());
        loadProfileImage(); // Load current profile picture on activity start

        loadUserName(currentUserId, welcomeTextView); // Pass currentUserId to loadUserName method
        loadFullName(currentUserId, fullTextView); // Pass currentUserId to loadUserName method
        loadDate(currentUserId, dateTextView); // Pass currentUserId to loadUserName method

        loadStarPoints(currentUserId, starpoints); // Pass currentUserId to loadUserName method
        loadSunPoints(currentUserId, sunpoints); // Pass currentUserId to loadUserName method




        TextView textView = findViewById(R.id.dailyStars);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Attendance.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });



        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        reportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Issue.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        Faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Faqs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        Notices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Notices.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Transactions.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, PromoCode.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });



        //navvar
        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        Store = findViewById(R.id.Store);
        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Storetop = findViewById(R.id.notification_icon);
        Storetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });


        noInternetLayout = findViewById(R.id.noInternetLayout);
        networkChangeReceiver = new ProfileChangeReceiver();

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
        loadProfileImage();
        loadUserName(currentUserId, welcomeTextView);
        loadFullName(currentUserId, fullTextView);
        loadDate(currentUserId, dateTextView);
        loadStarPoints(currentUserId, starpoints);
        loadSunPoints(currentUserId, sunpoints);


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





    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the logout action here
                        // For example, you can call a method to perform logout
                        performLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the cancel action
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Method to perform the logout operation
    private void performLogout() {
        startActivity(new Intent(this, Juans.class));
        finish();
    }









    private void loadStarPoints(String userId, TextView starpoints) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Kunin ang numerical value ng voting points
                        Long votingPoints = documentSnapshot.getLong("votingPoints");
                        if (votingPoints != null) {
                            // Convert to String bago i-set sa TextView
                            starpoints.setText(String.valueOf(votingPoints));
                        } else {
                            starpoints.setText("No voting points available");
                        }
                    } else {
                        // Handle case where document does not exist
                        starpoints.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    starpoints.setText("Failed to load voting points");
                    Log.e(TAG, "Error fetching voting points: " + e.getMessage());
                });
    }


    private void loadSunPoints(String userId, TextView sunpoints) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Kunin ang numerical value ng voting points
                        Long votingPoints = documentSnapshot.getLong("sunvotingpoints");
                        if (votingPoints != null) {
                            // Convert to String bago i-set sa TextView
                            sunpoints.setText(String.valueOf(votingPoints));
                        } else {
                            sunpoints.setText("No voting points available");
                        }
                    } else {
                        // Handle case where document does not exist
                        sunpoints.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    sunpoints.setText("Failed to load voting points");
                    Log.e(TAG, "Error fetching voting points: " + e.getMessage());
                });
    }





    private void loadDate(String userId, TextView welcomeTextView) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String signUpDate = documentSnapshot.getString("signUpDate");
                        dateTextView.setText(signUpDate); // Set the retrieved username to the TextView
                    } else {
                        // Handle case where document does not exist
                        dateTextView.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    dateTextView.setText("Failed to load username");
                    Log.e(TAG, "Error fetching username: " + e.getMessage());
                });
    }


    private void loadFullName(String userId, TextView welcomeTextView) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        fullTextView.setText(fullName); // Set the retrieved username to the TextView
                    } else {
                        // Handle case where document does not exist
                        fullTextView.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    fullTextView.setText("Failed to load username");
                    Log.e(TAG, "Error fetching username: " + e.getMessage());
                });
    }


    private void loadUserName(String userId, TextView welcomeTextView) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        welcomeTextView.setText(username); // Set the retrieved username to the TextView
                    } else {
                        // Handle case where document does not exist
                        welcomeTextView.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    welcomeTextView.setText("Failed to load username");
                    Log.e(TAG, "Error fetching username: " + e.getMessage());
                });
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Glide.with(this)
                        .load(imageUri)
                        .circleCrop() // Crop the image into a circle
                        .into(profileImageView); // Display selected image in ImageView

                uploadProfileImage(imageUri); // Upload selected image to Firebase Storage
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage(Uri imageUri) {
        // Initialize the LinearLayout and ProgressBar
        LinearLayout progressBarLayout = findViewById(R.id.progressbar); // LinearLayout containing the ProgressBar
        ProgressBar progressBar = findViewById(R.id.video_loading_progress); // ProgressBar

        progressBarLayout.setVisibility(View.VISIBLE); // Show LinearLayout with ProgressBar
        progressBar.setVisibility(View.VISIBLE); // Ensure ProgressBar is visible

        StorageReference fileReference = storageReference.child("profileImages").child(currentUserId + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String profileImageUrl = uri.toString();
                        saveProfileImageUrlToFirestore(profileImageUrl); // Save image URL to Firestore

                        // Update the ImageView with the new profile image
                        ImageView profileImageView = findViewById(R.id.profileImageView);
                        Glide.with(this) // Or Picasso.with(this)
                                .load(profileImageUrl)
                                .into(profileImageView);

                        progressBarLayout.setVisibility(View.GONE); // Hide LinearLayout with ProgressBar on success
                        showCustomToast("Change Profile Successfully", R.drawable.juanscast);
                    }).addOnFailureListener(e -> {
                        progressBarLayout.setVisibility(View.GONE); // Hide LinearLayout with ProgressBar on failure
                        showCustomToast("Failed to get download URL: " + e.getMessage(), R.drawable.juanscast);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBarLayout.setVisibility(View.GONE); // Hide LinearLayout with ProgressBar on failure
                    Toast.makeText(Profile.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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





    private void saveProfileImageUrlToFirestore(String profileImageUrl) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(currentUserId)
                .set(new UserProfile(profileImageUrl), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    showCustomToast("Image uploaded successfully " , R.drawable.juanscast);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Profile.this, "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadProfileImage() {
        // Show the progress bar while loading
        profileImageProgressBar.setVisibility(View.VISIBLE);

        storageReference.child("profileImages").child(currentUserId + ".jpg").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(Profile.this)
                            .load(uri)
                            .circleCrop() // Crop the image into a circle
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    // Hide the progress bar if image loading fails
                                    profileImageProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Hide the progress bar when the image is successfully loaded
                                    profileImageProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(profileImageView);
                })
                .addOnFailureListener(e -> {
                    // Hide the progress bar if the URL retrieval fails
                    profileImageProgressBar.setVisibility(View.GONE);

                    // Default image if no profile image found
                    FirebaseFirestore.getInstance().collection("User").document(currentUserId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String gender = documentSnapshot.getString("gender");
                                    if (gender != null) {
                                        if (gender.equals("Male")) {
                                            profileImageView.setImageResource(R.drawable.maledpc);
                                        } else if (gender.equals("Female")) {
                                            profileImageView.setImageResource(R.drawable.femaledp);
                                        }
                                    } else {
                                        // Gender not specified, show a default image for unknown gender
                                        profileImageView.setImageResource(R.drawable.user);
                                    }
                                } else {
                                    // Document for the user not found, show a default image
                                    profileImageView.setImageResource(R.drawable.user);
                                }
                            })
                            .addOnFailureListener(exception -> {
                                // Handle failure to retrieve user's gender
                                profileImageView.setImageResource(R.drawable.user);
                            });
                });
    }



    // Optional: Define a class to represent the UserProfile
    public static class UserProfile {
        private String profileImageUrl;

        public UserProfile() {
            // Default constructor required for Firestore
        }

        public UserProfile(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }



}
