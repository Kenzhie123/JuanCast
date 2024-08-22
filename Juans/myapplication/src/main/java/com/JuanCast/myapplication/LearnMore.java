package com.JuanCast.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LearnMore extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        textView = findViewById(R.id.textViewLearnMore);

        // Receive data from Intent
        String message = getIntent().getStringExtra("admin_message");
        textView.setText(message);
    }
}
