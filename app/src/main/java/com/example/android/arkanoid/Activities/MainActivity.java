package com.example.android.arkanoid.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    static public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        //show splashScreen
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }
}
