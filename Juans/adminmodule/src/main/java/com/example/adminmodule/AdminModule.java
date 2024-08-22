package com.JuanCast.adminmodule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class AdminModule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_module);

        Button sendButton = findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the text message to send
                String messageToSend = "Welcome to Learn More!"; // Replace with your actual message

                // Create an Intent to send data to LearnMore activity in MyApplication
                Intent intent = new Intent(getApplicationContext(), com.JuanCast.myapplication.LearnMore.class);
                intent.putExtra("admin_message", messageToSend);
                startActivity(intent);
            }
        });
    }
}
