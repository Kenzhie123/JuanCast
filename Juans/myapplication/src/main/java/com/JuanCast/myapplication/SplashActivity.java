package com.JuanCast.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private Handler handler = new Handler();
    private Runnable updateProgressRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.juancastobb);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });

        videoView.setOnCompletionListener(mp -> {
            handler.removeCallbacks(updateProgressRunnable);
            startActivity(new Intent(SplashActivity.this, Juans.class));
            overridePendingTransition(0, 0); // No animation
            finish();
        });


    }


}
