package com.JuanCast.myapplicationadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.JuanCast.myapplication.LearnMore;


public class MainActivityAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Intent intent = new Intent(this, LearnMore.class);
        intent.putExtra("textDesign", "Hello, Application!");
        startActivity(intent);

    }
}