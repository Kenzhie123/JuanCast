package com.JuanCast.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Setting extends AppCompatActivity {

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    private ImageView back;

    //about
    private LinearLayout about1;
    private ImageView about2;
    private TextView about3;
    private LinearLayout about4;
    private ImageView about5;



    //privacy
    private LinearLayout privacy1;
    private ImageView privacy2;
    private TextView privacy3;
    private LinearLayout privacy4;
    private ImageView privacy5;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        about1 = findViewById(R.id.about1);
        about2 = findViewById(R.id.about2);
        about3 = findViewById(R.id.about3);
        about4 = findViewById(R.id.about4);
        about5 = findViewById(R.id.about5);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });


        about1.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, About.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        about2.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, About.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        about3.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, About.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        about4.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, About.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        about5.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, About.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });





        privacy1 = findViewById(R.id.privacy1);
        privacy2 = findViewById(R.id.privacy2);
        privacy3 = findViewById(R.id.privacy3);
        privacy4 = findViewById(R.id.privacy4);
        privacy5 = findViewById(R.id.privacy5);

        //privacy
        privacy1.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        //privacy
        privacy2.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        //privacy
        privacy3.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        //privacy
        privacy4.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        //privacy
        privacy5.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PrivacyPolicy.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });




        //navvar

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Button deactivateButton = findViewById(R.id.deactivateButton);
        deactivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
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



    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deactivation")
                .setMessage("Are you sure you want to deactivate your account? You can reactivate it within 30 days, after which your account will be permanently deleted.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deactivateAccount();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deactivateAccount() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            long deactivationTime = System.currentTimeMillis();
            long deleteAfterMillis = 30 * 24 * 60 * 60 * 1000; // 30 days in milliseconds
            long deletionTimestamp = deactivationTime + deleteAfterMillis;

            DocumentReference userRef = firebaseFirestore.collection("User").document(user.getUid());

            userRef.update("status", "deactivated", "deactivationTime", deactivationTime, "deletionTimestamp", deletionTimestamp)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User account deactivated in Firestore.");
                                // Redirect to JuansClass
                                Intent intent = new Intent(Setting.this, Juans.class);
                                startActivity(intent);
                                finish(); // Optional: Finish this activity so the user can't go back to it
                            } else {
                                Log.d("TAG", "Error updating user status: ", task.getException());
                                Toast.makeText(Setting.this, "Error deactivating account.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.d("TAG", "No user is signed in.");
            Toast.makeText(Setting.this, "No user is signed in.", Toast.LENGTH_SHORT).show();
        }
    }


    public void checkUserStatusBeforeLogin(FirebaseUser user) {
        DocumentReference userRef = firebaseFirestore.collection("User").document(user.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String status = document.getString("status");
                        long deactivationTime = document.getLong("deactivationTime");
                        long deletionTimestamp = document.getLong("deletionTimestamp");
                        long currentTime = System.currentTimeMillis();

                        if ("deactivated".equals(status)) {
                            if (currentTime < deletionTimestamp) {
                                // Account is deactivated but deletion is not yet due
                                long remainingTimeMillis = deletionTimestamp - currentTime;
                                long remainingDays = remainingTimeMillis / (24 * 60 * 60 * 1000);
                                Toast.makeText(Setting.this, "Account deactivated. Please try again in " + remainingDays + " days.", Toast.LENGTH_LONG).show();
                            } else {
                                // Deletion time has passed, proceed with account deletion
                                deleteUserAccount(user);
                            }
                        } else {
                            // Account is not deactivated, proceed with login
                            Toast.makeText(Setting.this, "Account is active.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("TAG", "No such document.");
                        Toast.makeText(Setting.this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("TAG", "Error getting document: ", task.getException());
                    Toast.makeText(Setting.this, "Error checking user status.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUserAccount(FirebaseUser user) {
        DocumentReference userRef = firebaseFirestore.collection("User").document(user.getUid());
        userRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User account permanently deleted.");
                                Toast.makeText(Setting.this, "Account has been permanently deleted.", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("TAG", "Error deleting user: ", task.getException());
                                Toast.makeText(Setting.this, "Error deleting account.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.d("TAG", "Error deleting user data from Firestore: ", task.getException());
                    Toast.makeText(Setting.this, "Error deleting user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetUserStatus(FirebaseUser user) {
        DocumentReference userRef = firebaseFirestore.collection("User").document(user.getUid());
        userRef.update("status", "active", "deactivationTime", null, "deletionTimestamp", null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User status reset to active.");
                            Toast.makeText(Setting.this, "Account is now active. You can log in.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("TAG", "Error resetting user status: ", task.getException());
                            Toast.makeText(Setting.this, "Error resetting user status.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
