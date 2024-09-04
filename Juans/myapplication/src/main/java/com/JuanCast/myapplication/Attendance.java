package com.JuanCast.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance extends AppCompatActivity {

    private SharedPreferences prefs;
    private TextView textStreak;
    private Button[] dayButtons = new Button[7];

    private String currentUserId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private TextView starpoints;
    private TextView sunpoints;

    private ImageView back;

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;


    private static final int REWARD_POINTS = 20; // Updated reward points

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize Firebase instances
        initializeFirebase();

        // Load SharedPreferences for the current user
        prefs = getUserPrefs(currentUserId);

        // Initialize UI elements
        initializeUI();

        // Initialize day buttons and handle reward claiming
        initializeDayButtons();

        // Setup weekly reset logic (Monday to Sunday)
        setupWeeklyReset();

        // Check if it's the user's first login and claim day 1 reward
        checkFirstLoginAndClaim();

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });



        //navvar

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, Profile.class);
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


    @Override
    protected void onResume() {
        super.onResume();

        // Reload SharedPreferences for the current user
        prefs = getUserPrefs(currentUserId);

        // Update UI based on claimed button states
        handleRewardClaiming();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance
        firebaseFirestore = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore instance

        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
        }
    }

    private SharedPreferences getUserPrefs(String userId) {
        return getSharedPreferences("attendance_prefs_" + userId, MODE_PRIVATE);
    }

    private void initializeUI() {
        textStreak = findViewById(R.id.text_streak);
        starpoints = findViewById(R.id.starpoints);
        sunpoints = findViewById(R.id.sunpoints);

        loadPoints(currentUserId); // Load both star and sun points for the user
        updateStreakText(); // Update streak text based on last login
    }

    private void loadPoints(String userId) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Retrieve voting points
                            Long votingPoints = document.getLong("votingPoints"); // Correct field name
                            if (votingPoints != null) {
                                starpoints.setText(String.valueOf(votingPoints));
                            } else {
                                starpoints.setText("No star points available");
                            }

                            // Retrieve sun voting points
                            Long sunVotingPoints = document.getLong("sunvotingpoints"); // Correct field name
                            if (sunVotingPoints != null) {
                                sunpoints.setText(String.valueOf(sunVotingPoints));
                            } else {
                                sunpoints.setText("No sun points available");
                            }
                        } else {
                            Log.d("Attendance", "No such document");
                        }
                    } else {
                        Log.d("Attendance", "get failed with ", task.getException());
                    }
                });
    }

    private void initializeDayButtons() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < 7; i++) {
            int buttonId = getResources().getIdentifier("btn_claim_day" + (i + 1), "id", getPackageName());
            dayButtons[i] = findViewById(buttonId);

            final int dayIndex = i;
            dayButtons[i].setOnClickListener(v -> claimRewardForDay(dayIndex));

            boolean claimed = prefs.getBoolean("button_claimed_" + dayIndex, false);

            if (claimed) {
                dayButtons[i].setText("Claimed");
                dayButtons[i].setEnabled(false);
                dayButtons[i].setBackgroundResource(R.drawable.buttonwhite);
                dayButtons[i].setTextColor(Color.BLACK);
            } else if (currentDayOfWeek == getDayOfWeekIndex(dayIndex)) {
                dayButtons[i].setText("Claim");
                dayButtons[i].setEnabled(true);
                dayButtons[i].setBackgroundResource(R.drawable.buttonblue);
                dayButtons[i].setTextColor(Color.WHITE);
            } else {
                dayButtons[i].setText("Not Ready");
                dayButtons[i].setEnabled(false);
                dayButtons[i].setBackgroundResource(R.drawable.buttondark);
                dayButtons[i].setTextColor(Color.WHITE);
            }
        }
    }



    private void setupWeeklyReset() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Find the next Monday
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilNextMonday = (Calendar.MONDAY - dayOfWeek + 7) % 7;
        if (daysUntilNextMonday == 0) {
            daysUntilNextMonday = 7; // If today is Monday, schedule for the next Monday
        }

        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextMonday);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long resetTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        long delayMillis = resetTime - currentTime;

        // Schedule the weekly reset
        scheduleWeeklyReset(delayMillis);
    }


    private void scheduleWeeklyReset(long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            resetWeeklyClaimsForAllUsers(); // Trigger reset for all users
            // Re-schedule the reset for the following week
            setupWeeklyReset();
        }, delayMillis);
    }



    private void resetWeeklyClaimsForAllUsers() {
        firebaseFirestore.collection("User").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Reset claimed days for each user
                            firebaseFirestore.collection("User").document(document.getId())
                                    .update("claimedDays", getDefaultClaimedDays())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Attendance", "Successfully reset claims for user: " + document.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Attendance", "Error resetting claims for user: " + document.getId(), e);
                                    });
                        }
                    } else {
                        Log.d("Attendance", "Failed to fetch users", task.getException());
                    }
                });
    }

    private Map<String, Boolean> getDefaultClaimedDays() {
        Map<String, Boolean> claimedDays = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            claimedDays.put(String.valueOf(i), false); // Reset all days to false
        }
        return claimedDays;
    }


    private void resetWeeklyClaims() {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < 7; i++) {
            editor.putBoolean("button_claimed_" + i, false);
            editor.putLong("button_last_claim_time_" + i, 0);

            // Update UI accordingly
            dayButtons[i].setText("Claim");
            dayButtons[i].setEnabled(true);
            dayButtons[i].setBackgroundResource(R.drawable.buttonblue); // Reset to default button color
            dayButtons[i].setTextColor(Color.WHITE); // Reset text color
        }
        editor.apply();
        Toast.makeText(this, "Weekly reset completed.", Toast.LENGTH_SHORT).show();
    }


    private void checkFirstLoginAndClaim() {
        // Check if it's the user's first login (based on shared preferences)
        boolean isFirstLogin = prefs.getBoolean("first_login", true);

        if (isFirstLogin) {
            // Update first login status to false (since it's already claimed)
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_login", false); // Update first login status
            editor.apply();
        }
    }

    private void claimRewardForDay(int dayIndex) {
        // Implement reward claiming logic for the specific day
        boolean claimed = prefs.getBoolean("button_claimed_" + dayIndex, false);
        if (claimed) {
            // Button has already been claimed, show a toast message
            Toast.makeText(this, "You have already claimed the reward for day " + (dayIndex + 1), Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with claiming logic
            Calendar calendar = Calendar.getInstance();
            int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Check if the current day matches the claim day index
            if (currentDayOfWeek == getDayOfWeekIndex(dayIndex)) {
                long lastClaimTime = prefs.getLong("button_last_claim_time_" + dayIndex, 0);
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClaimTime >= 24 * 60 * 60 * 1000) {
                    // Sufficient time has passed since last claim
                    showClaimConfirmationDialog(dayIndex); // Show confirmation dialog to claim reward
                } else {
                    // Not enough time has passed, show a toast message with when they can claim
                    calendar.setTimeInMillis(lastClaimTime + 24 * 60 * 60 * 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String claimTime = dateFormat.format(calendar.getTime());

                    // Show custom Toast with image
                    showCustomToast("You can claim this reward again tomorrow at " + claimTime, R.drawable.juanscast); // Palitan ang R.drawable.ic_info sa iyong image resource ID
                }
            } else {
                // Current day does not match the claim day index, show a toast message
                String dayOfWeek = getDayOfWeek(dayIndex);
                // Show custom Toast with image
                showCustomToast("You can only claim the reward for " + dayOfWeek, R.drawable.juanscast); // Palitan ang R.drawable.ic_warning sa iyong image resource ID
            }
        }
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


    private void showClaimConfirmationDialog(int dayIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Claim Reward");
        builder.setMessage("Are you sure you want to claim the reward for " + getDayOfWeek(dayIndex) + "?");
        builder.setPositiveButton("Claim", (dialog, which) -> {
            // Mark the button as claimed
            markButtonAsClaimed(dayIndex);

            // Update voting points for claiming the reward (change from 10 to 20)
            updateVotingPoints(REWARD_POINTS); // Update voting points for claiming the reward
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void markButtonAsClaimed(int dayIndex) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("button_claimed_" + dayIndex, true);
        editor.putLong("button_last_claim_time_" + dayIndex, System.currentTimeMillis());
        editor.apply();

        // Change button text to "Claimed" and text color to white
        dayButtons[dayIndex].setText("Claimed");
        dayButtons[dayIndex].setTextColor(Color.BLACK); // Set text color to white
        dayButtons[dayIndex].setEnabled(false); // Optionally disable the button
        dayButtons[dayIndex].setBackgroundResource(R.drawable.buttonwhite); // Set background color to dark
    }

    private void updateVotingPoints(int pointsToAdd) {
        // Fetch current voting points from Firestore and update
        firebaseFirestore.collection("User").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve current points
                        Long currentPoints = documentSnapshot.getLong("votingPoints");

                        if (currentPoints != null) {
                            // Calculate new total points
                            long newPoints = currentPoints + pointsToAdd;

                            // Update points in Firestore
                            updatePointsInFirestore(newPoints);
                        } else {
                            Log.d("Attendance", "votingPoints field is null");
                            Toast.makeText(this, "Failed to update voting points - Voting points field is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("Attendance", "Document does not exist");
                        Toast.makeText(this, "Failed to update voting points - User document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that may occur when fetching current points
                    Log.w("Attendance", "Error fetching current voting points", e);
                    Toast.makeText(this, "Failed to fetch current voting points", Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePointsInFirestore(long newPoints) {
        // Update points in Firestore
        firebaseFirestore.collection("User").document(currentUserId)
                .update("votingPoints", newPoints)
                .addOnSuccessListener(aVoid -> {
                    // Update UI or perform additional tasks upon successful update
                    Log.d("Attendance", "Voting points updated successfully");
                    loadPoints(currentUserId); // Reload points to update UI
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that may occur
                    Log.w("Attendance", "Error updating voting points", e);
                    Toast.makeText(this, "Failed to update voting points", Toast.LENGTH_SHORT).show();
                });
    }

    private String getDayOfWeek(int dayIndex) {
        switch (dayIndex) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "";
        }
    }

    private int getDayOfWeekIndex(int dayIndex) {
        switch (dayIndex) {
            case 0:
                return Calendar.MONDAY;
            case 1:
                return Calendar.TUESDAY;
            case 2:
                return Calendar.WEDNESDAY;
            case 3:
                return Calendar.THURSDAY;
            case 4:
                return Calendar.FRIDAY;
            case 5:
                return Calendar.SATURDAY;
            case 6:
                return Calendar.SUNDAY;
            default:
                return -1; // Invalid day index
        }
    }

    private void handleRewardClaiming() {
        // Update UI based on claimed button states
        for (int i = 0; i < 7; i++) {
            boolean claimed = prefs.getBoolean("button_claimed_" + i, false);

            if (claimed) {
                // Button is claimed, change its text and disable if necessary
                dayButtons[i].setText("Claimed");
                dayButtons[i].setEnabled(false); // Disable the button if desired
            } else {
                // Button is not claimed, show it with original text
                dayButtons[i].setText("Claim"); // Change text to "Claim"
                dayButtons[i].setEnabled(true); // Enable the button
            }
        }
    }


    private void updateStreakText() {
        int currentStreak = calculateStreak();
        textStreak.setText("Your current streak: " + currentStreak + " days");
    }

    private int calculateStreak() {
        SharedPreferences userPrefs = getUserPrefs(currentUserId);

        long lastLoginTime = userPrefs.getLong("last_login_time", 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastLoginTime);
        int lastLoginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        if (currentDayOfYear != lastLoginDayOfYear) {
            // Reset streak logic here (if needed)
            int streak = userPrefs.getInt("current_streak", 0);
            if (currentDayOfYear - lastLoginDayOfYear == 1) {
                streak++;
            } else {
                streak = 1; // Reset streak
            }
            SharedPreferences.Editor editor = userPrefs.edit();
            editor.putInt("current_streak", streak);
            editor.putLong("last_login_time", System.currentTimeMillis());
            editor.apply();
            return streak;
        } else {
            return userPrefs.getInt("current_streak", 0);
        }
    }
}
