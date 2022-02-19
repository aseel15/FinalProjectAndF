package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LogOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        SharedPreferences preferences;

        preferences = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("login", -1);
        editor.putInt("role",-1);
        editor.apply();

        Intent intent=new Intent(LogOut.this,SignIn.class);
        startActivity(intent);






    }
}