package com.JuanCast.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ads extends AppCompatActivity {

    private static final String TAG = "ads";
    private FirebaseFirestore firebaseFirestore;
    private  ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Map<Integer, Integer> adCountsMap = new HashMap<>();

    private static final long AD_COOLDOWN_MILLIS = 60000; // 1 minute cooldown
    private static final String PREF_LAST_AD_TIME = "lastAdTime";
    private Handler handler = new Handler(Looper.getMainLooper());

    private Handler cooldownHandler = new Handler();
    private Runnable cooldownRunnable;



    private static final int MAX_ADS_PER_COOLDOWN = 15;
    private static final long COOLDOWN_PERIOD_MILLIS = TimeUnit.MINUTES.toMillis(1); // 1 minute cooldown


    private SharedPreferences sharedPreferences;

    private TextView tvAvailableStars;
    private TextView tvAdsWheelsCount;
    private TextView tvAdsCpCount;
    private TextView tvAdsPuzzleCount;
    private TextView tvAdsStarCount;

    private String currentUserId;
    private FirebaseUser user;


    private FirebaseAuth firebaseAuth;



    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        ImageView logo = findViewById(R.id.logo);

        tvAdsCpCount = findViewById(R.id.tvAdsCpCount);
        tvAdsWheelsCount = findViewById(R.id.tvAdsWheelsCount);
        tvAdsPuzzleCount = findViewById(R.id.tvAdsPuzzleCount);
        tvAdsStarCount = findViewById(R.id.tvAdsStarCount);
        tvAvailableStars = findViewById(R.id.tvAvailableStars);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        restoreAndDisplayCooldownTimers();




        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Homepage.class); // Replace CurrentActivity with your current activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                overridePendingTransition(0, 0); // Walang animation
                startActivity(intent);
                finish();
            }
        });

        //navvar
        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        Store = findViewById(R.id.profile);
        Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView tvSuns = findViewById(R.id.tvSuns);
        tvSuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Store.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ads = findViewById(R.id.tvStars);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ups = findViewById(R.id.tvPowerUps);
        ups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ads.this, Powerups.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
            loadStarPoints(currentUserId, tvAvailableStars);
        } else {
            Log.e(TAG, "User not authenticated.");
        }

        sharedPreferences = getSharedPreferences("ad_count_pref", MODE_PRIVATE);

        MobileAds.initialize(this, initializationStatus -> {
            Log.d(TAG, "AdMob initialized successfully.");
            loadInterstitialAd(R.id.ButtonCp, "ca-app-pub-6114080410158173/5076397663");
            loadInterstitialAd(R.id.ButtonWheels, "ca-app-pub-6114080410158173/7953477486");
            loadInterstitialAd(R.id.ButtonPuzzle, "ca-app-pub-6114080410158173/8935386669");
            loadInterstitialAd(R.id.ButtonStar, "ca-app-pub-6114080410158173/3195974826");
        });

        // Scheduler initialization
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleDailyReset(this::resetAllAdCounts, 23, 59); // 11:59 PM



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    // Schedule a task to run daily at a specified time
    private void scheduleDailyReset(Runnable task, int hourOfDay, int minuteOfHour) {
        long initialDelay = getInitialDelay(hourOfDay, minuteOfHour);
        long period = TimeUnit.DAYS.toMillis(1); // 24 hours in milliseconds

        Log.d(TAG, "Scheduling task with initial delay: " + initialDelay + " milliseconds");

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private long getInitialDelay(int hourOfDay, int minuteOfHour) {
        Calendar now = Calendar.getInstance();
        Calendar nextRun = Calendar.getInstance();

        nextRun.set(Calendar.HOUR_OF_DAY, hourOfDay);
        nextRun.set(Calendar.MINUTE, minuteOfHour);
        nextRun.set(Calendar.SECOND, 59);
        nextRun.set(Calendar.MILLISECOND, 0);

        // If it's past the specified time today, schedule for the next day
        if (now.after(nextRun)) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }

        return nextRun.getTimeInMillis() - now.getTimeInMillis();
    }

    // Reset all ad counts
    private void resetAllAdCounts() {
        Log.d(TAG, "Resetting all ad counts at: " + new Date().toString());
        firebaseFirestore.collection("AdCounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            resetAdCountForDocument(document);
                        }
                    } else {
                        Log.e(TAG, "Error retrieving documents: " + task.getException());
                    }
                });
    }

    // Reset the ad count for each document in Firestore
    private void resetAdCountForDocument(DocumentSnapshot document) {
        String userId = document.getString("userId");
        int imageViewId = document.getLong("imageViewId").intValue();

        // Reset ad count to 0
        updateAdCountsInFirestore(userId, imageViewId, 0);
    }

    private void updateAdCountsInFirestore(String userId, int imageViewId, int adCount) {
        Map<String, Object> adCountData = new HashMap<>();
        adCountData.put("userId", userId);
        adCountData.put("imageViewId", imageViewId);
        adCountData.put("adCount", adCount);
        adCountData.put("timestamp", Timestamp.now()); // Current timestamp

        firebaseFirestore.collection("AdCounts")
                .document(userId + "_" + imageViewId)
                .set(adCountData)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Ad count updated successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating ad count: " + e.getMessage()));
    }


    // Load and set up ads for each ImageView
    private void loadInterstitialAd(int imageViewId, String adUnitId) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                Log.d(TAG, "Interstitial ad loaded successfully for ImageView ID: " + imageViewId);
                initializeImageView(imageViewId, ad);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Log.e(TAG, "Failed to load interstitial ad for ImageView ID: " + imageViewId + " - " + adError.getMessage());
                showCustomToast("Failed to load ad. Please try again later.", R.drawable.juanscast);
            }
        });
    }



    private void initializeImageView(int imageViewId, InterstitialAd ad) {
        ImageView imageView = findViewById(imageViewId);
        adCountsMap.put(imageViewId, 0);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AdPreferences", MODE_PRIVATE);

        imageView.setOnClickListener(view -> {
            long currentTime = System.currentTimeMillis();
            long lastAdTime = sharedPreferences.getLong(getLastAdTimeKey(imageViewId), 0);
            long elapsedTime = currentTime - lastAdTime;

            if (elapsedTime >= AD_COOLDOWN_MILLIS) {
                if (canShowAd(imageViewId)) {
                    if (ad != null) {
                        showCustomToast("Loading ad...", R.drawable.juanscast);

                        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                incrementAdCount(imageViewId);
                                logAdView(30);
                                loadInterstitialAd(imageViewId, ad.getAdUnitId()); // Reload the ad for future use

                                // Update the last ad time
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong(getLastAdTimeKey(imageViewId), System.currentTimeMillis());
                                editor.apply();

                                // Update cooldown TextView
                                updateCooldownTextView(imageViewId);
                                // Start periodic updates
                                startCooldownUpdates(imageViewId);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d(TAG, "Ad failed to show: " + adError.getMessage());
                                showCustomToast("Failed to load ad.", R.drawable.juanscast);
                                loadInterstitialAd(imageViewId, ad.getAdUnitId()); // Optionally reload ad
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Optional: You can implement actions here when the ad starts showing
                            }
                        });

                        ad.show(ads.this);
                    } else {
                        Log.d(TAG, "The interstitial ad wasn't ready yet for ImageView ID: " + imageViewId);
                        showCustomToast("Ads loading, please wait.", R.drawable.juanscast);
                        loadInterstitialAd(imageViewId, ad.getAdUnitId()); // Optionally reload ad
                    }
                } else {
                    showCustomToast("You need to wait 1 minute before watching another ad.", R.drawable.juanscast);
                }

                updateAdCountTextView(imageViewId);
            } else {
                showCustomToast("You need to wait 1 minute before watching another ad.", R.drawable.juanscast);
            }
        });
    }


    private void updateCooldownTextView(int imageViewId) {
        TextView cooldownTextView = getCooldownTextView(imageViewId);
        if (cooldownTextView != null) {
            long currentTime = System.currentTimeMillis();
            long lastAdTime = sharedPreferences.getLong(getLastAdTimeKey(imageViewId), 0);
            long timeRemaining = AD_COOLDOWN_MILLIS - (currentTime - lastAdTime);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(getRemainingCooldownKey(imageViewId), timeRemaining);
            editor.apply();

            if (timeRemaining > 0) {
                long minutes = timeRemaining / 60000;
                long seconds = (timeRemaining % 60000) / 1000;
                String timeRemainingText = String.format("Cooldown: %02d:%02d", minutes, seconds);
                cooldownTextView.setText(timeRemainingText);
            } else {
                cooldownTextView.setText("Cooldown: 00:00");
            }
        }
    }

    private void restoreCooldownState(int imageViewId) {
        TextView cooldownTextView = getCooldownTextView(imageViewId);
        if (cooldownTextView != null) {
            long remainingTime = sharedPreferences.getLong(getRemainingCooldownKey(imageViewId), 0);
            if (remainingTime > 0) {
                long minutes = remainingTime / 60000;
                long seconds = (remainingTime % 60000) / 1000;
                String timeRemainingText = String.format("Cooldown: %02d:%02d", minutes, seconds);
                cooldownTextView.setText(timeRemainingText);
            } else {
                cooldownTextView.setText("Cooldown: 00:00");
            }
        }
    }


    private TextView getCooldownTextView(int imageViewId) {
        if (imageViewId == R.id.ButtonCp) {
            return findViewById(R.id.tvCooldownCp);
        } else if (imageViewId == R.id.ButtonWheels) {
            return findViewById(R.id.tvCooldownWheels);
        } else if (imageViewId == R.id.ButtonPuzzle) {
            return findViewById(R.id.tvCooldownPuzzle);
        } else if (imageViewId == R.id.ButtonStar) {
            return findViewById(R.id.tvCooldownStar);
        }
        return null;
    }

    private String getRemainingCooldownKey(int imageViewId) {
        return "RemainingCooldown_" + imageViewId;
    }

    private void restoreAndDisplayCooldownTimers() {
        for (int id : adCountsMap.keySet()) {
            restoreCooldownState(id);
            startCooldownUpdates(id); // Start the periodic updates for the cooldown
        }
    }




    private void startCooldownUpdates(final int imageViewId) {
        cooldownRunnable = new Runnable() {
            @Override
            public void run() {
                updateCooldownTextView(imageViewId);
                cooldownHandler.postDelayed(this, 1000); // Update every second
            }
        };
        cooldownHandler.post(cooldownRunnable);
    }

    private void stopCooldownUpdates() {
        if (cooldownRunnable != null) {
            cooldownHandler.removeCallbacks(cooldownRunnable);
        }
    }


    private String getLastAdTimeKey(int imageViewId) {
        return "LastAdTime_" + imageViewId;
    }



    @Override
    protected void onResume() {
        super.onResume();


        if (user != null) {
            fetchAdCountsFromFirestore(currentUserId, R.id.ButtonCp);
            fetchAdCountsFromFirestore(currentUserId, R.id.ButtonWheels);
            fetchAdCountsFromFirestore(currentUserId, R.id.ButtonPuzzle);
            fetchAdCountsFromFirestore(currentUserId, R.id.ButtonStar);
        }

        for (int id : adCountsMap.keySet()) {
            restoreCooldownState(id);
            startCooldownUpdates(id);
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

    private void showCustomToasts(String message, int drawableResId) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundResource(drawableResId);
        toast.show();
    }






    private void loadStarPoints(String userId, TextView tvAvailableStars) {
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

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    private void logAdView(int rewardPoints) {
        Map<String, Object> adViewData = new HashMap<>();
        adViewData.put("userId", currentUserId);
        adViewData.put("date", getCurrentDate());
        adViewData.put("time", getCurrentTime());
        adViewData.put("rewardPoints", rewardPoints);  // Add reward points to the log data
        adViewData.put("timestamp", Timestamp.now());  // Get the current timestamp

        firebaseFirestore.collection("AdViews")
                .add(adViewData)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "Ad view logged successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error logging ad view: " + e.getMessage()));
    }







    private void fetchAdCountsFromFirestore(String userId, int imageViewId) {
        firebaseFirestore.collection("AdCounts")
                .document(userId + "_" + imageViewId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Integer adCount = documentSnapshot.getLong("adCount").intValue();
                        if (adCount != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("ad_count_" + userId + "_" + imageViewId, adCount);
                            editor.apply();
                            updateAdCountTextView(imageViewId);
                        }
                    } else {
                        // If document does not exist, initialize it
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("ad_count_" + userId + "_" + imageViewId, 0);
                        editor.apply();
                        updateAdCountTextView(imageViewId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching ad count: " + e.getMessage());
                });
    }




    private boolean canShowAd(int imageViewId) {
        // Get the current ad count for the user and image view
        int adCount = sharedPreferences.getInt("ad_count_" + currentUserId + "_" + imageViewId, 0);

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate the start of today (midnight) in milliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDayMillis = calendar.getTimeInMillis();

        // Calculate the start of the next day (midnight) in milliseconds
        long startOfNextDayMillis = startOfDayMillis + TimeUnit.DAYS.toMillis(1);

        // Get the stored cooldown end time or default to the start of the next day if not set
        long cooldownEndTimeMillis = sharedPreferences.getLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, startOfNextDayMillis);

        // If it's a new day, reset ad count and cooldown
        if (currentTimeMillis >= startOfNextDayMillis) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, startOfNextDayMillis);
            editor.putInt("ad_count_" + currentUserId + "_" + imageViewId, 0); // Reset ad count for the new day
            editor.apply();

            // Allow ad to be shown
            return true;
        } else if (adCount < MAX_ADS_PER_COOLDOWN) {
            // Ad count is less than MAX_ADS_PER_COOLDOWN, allow ad to be shown without cooldown
            return true;
        } else {
            // Calculate time remaining until the next cooldown reset (midnight)
            long timeUntilMidnight = startOfNextDayMillis - currentTimeMillis;
            long hoursRemaining = TimeUnit.MILLISECONDS.toHours(timeUntilMidnight);
            long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(timeUntilMidnight) % 60;
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(timeUntilMidnight) % 60;

            // Show the countdown message
            String countdownMessage = String.format("You can watch another ad in %d hours %d minutes %d seconds.", hoursRemaining, minutesRemaining, secondsRemaining);
            showCustomToast(countdownMessage, R.drawable.juanscast);

            return false;
        }
    }


    private void incrementAdCount(int imageViewId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();

        // Get the start of the current day (midnight)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDayMillis = calendar.getTimeInMillis();

        // Get the last cooldown end time from SharedPreferences
        long lastCooldownEndTime = sharedPreferences.getLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, 0);

        // Check if the current time is past midnight and the cooldown end time is less than the start of today
        if (currentTimeMillis >= startOfDayMillis && lastCooldownEndTime < startOfDayMillis) {
            // Reset ad count and cooldown end time for the new day
            editor.putInt("ad_count_" + currentUserId + "_" + imageViewId, 0);
            editor.putLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, 0);
        }

        // Get the current ad count and increment it
        int adCount = sharedPreferences.getInt("ad_count_" + currentUserId + "_" + imageViewId, 0) + 1;
        editor.putInt("ad_count_" + currentUserId + "_" + imageViewId, adCount);

        // Check if ad count has reached the maximum
        if (adCount >= MAX_ADS_PER_COOLDOWN) {
            // Set cooldown period
            editor.putLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, currentTimeMillis + COOLDOWN_PERIOD_MILLIS);
            showCustomToast("You have reached the maximum ad limit for this option. Please wait for the cooldown period.", R.drawable.juanscast);
        }

        editor.apply();

        // Update points and show toast
        updatePointsAndShowToast(adCount);

        // Update ad counts in Firestore
        updateAdCountsInFirestore(currentUserId, imageViewId, adCount);

        // Reset ad count and cooldown end time after cooldown period
        if (adCount >= MAX_ADS_PER_COOLDOWN) {
            new Handler().postDelayed(() -> {
                editor.putInt("ad_count_" + currentUserId + "_" + imageViewId, 0);
                editor.putLong("cooldown_end_time_" + currentUserId + "_" + imageViewId, 0);
                editor.apply();
                updateAdCountTextView(imageViewId);
            }, COOLDOWN_PERIOD_MILLIS + 1000); // Add a small delay (1 second) to ensure cooldown has ended
        }
    }


    private void updatePointsAndShowToast(int adCount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Claim Your Reward");
        builder.setMessage(" Claim Your Rewards");

        builder.setPositiveButton("Claim", (dialog, which) -> {
            firebaseFirestore.collection("User").document(currentUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Long currentPoints = documentSnapshot.getLong("votingPoints");
                            if (currentPoints != null) {
                                long newPoints = currentPoints + calculateReward(adCount);

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("votingPoints", newPoints);

                                firebaseFirestore.collection("User").document(currentUserId)
                                        .update(updates)
                                        .addOnSuccessListener(aVoid -> {
                                            tvAvailableStars.setText(String.valueOf(newPoints));
                                            showCustomToast("You earned " + calculateReward(adCount) + " voting points!", R.drawable.juanscast);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to update voting points: " + e.getMessage());
                                            showCustomToast("Failed to add points. Please try again later.", R.drawable.juanscast);
                                        });
                            } else {
                                Log.e(TAG, "Current voting points is null.");
                            }
                        } else {
                            Log.e(TAG, "User document does not exist.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching voting points: " + e.getMessage());
                        Toast.makeText(ads.this, "Failed to add points. Please try again later.", Toast.LENGTH_SHORT).show();
                    });
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private int calculateReward(int adCount) {
        // You can adjust the reward calculation based on your app's logic
        return 30; // For example, giving 30 voting points per ad watched
    }

    private void updateAdCountTextView(int imageViewId) {
        int adCount = sharedPreferences.getInt("ad_count_" + currentUserId + "_" + imageViewId, 0);
        String text = "Watch Ad (" + adCount + "/" + MAX_ADS_PER_COOLDOWN + ")";

        if (imageViewId == R.id.ButtonCp) {
            tvAdsCpCount.setText(text);
        } else if (imageViewId == R.id.ButtonWheels) {
            tvAdsWheelsCount.setText(text);
        } else if (imageViewId == R.id.ButtonPuzzle) {
            tvAdsPuzzleCount.setText(text);
        } else if (imageViewId == R.id.ButtonStar) {
            tvAdsStarCount.setText(text);
        }
    }

}

