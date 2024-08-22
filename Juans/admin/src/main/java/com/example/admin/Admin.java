package com.JuanCast.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.LearnMore;


public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView textViewAdminMessage = findViewById(R.id.textViewAdminMessage);
// Use textViewAdminMessage as needed


        // Set onClickListener to textViewAdminMessage
        textViewAdminMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to launch LearnMore activity
                Intent intent = new Intent(Admin.this, LearnMore.class);
                intent.putExtra("admin_message", "Welcome from Admin Module!");
                startActivity(intent);
            }
        });

    }
}