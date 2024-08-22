package com.JuanCast.myadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.LearnMore;


public class MyAdmin extends AppCompatActivity {

    private EditText editTextInput;
    private Button buttonSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_admin);

        editTextInput = findViewById(R.id.editTextInput);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = editTextInput.getText().toString().trim();

                // Start Module 2 activity and pass the user input as extra
                Intent intent = new Intent(MyAdmin.this, LearnMore.class);
                intent.putExtra("userInput", userInput);
                startActivity(intent);
            }
        });



    }
}