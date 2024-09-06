package com.JuanCast.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class MonthResetPoints extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_reset_points);

        // Schedule the ResetVotingPointsWorker to run monthly
        PeriodicWorkRequest resetWorkRequest =
                new PeriodicWorkRequest.Builder(ResetVotingPointsWorker.class, 30, TimeUnit.DAYS)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "resetVotingPoints",
                ExistingPeriodicWorkPolicy.KEEP,
                resetWorkRequest
        );
    }
}