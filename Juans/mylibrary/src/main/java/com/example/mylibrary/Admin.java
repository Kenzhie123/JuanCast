package com.JuanCast.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button libraryButton = findViewById(R.id.library_button);

        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("com.example.myapp.SHOW_TEXTVIEW");
                    sendBroadcast(intent);
                    Toast.makeText(Admin.this, "Intent sent successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Admin.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
