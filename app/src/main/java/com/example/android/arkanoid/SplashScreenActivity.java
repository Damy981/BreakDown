package com.example.android.arkanoid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private FirebaseAuth mAuth;
    String activityToStart;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update the string with the right class name.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            activityToStart = "com.example.android.arkanoid.LoginActivity";
        }
        else {
            activityToStart = "com.example.android.arkanoid.MenuActivity";
        }

        /* New Handler to start the Menu-Activity
           and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu or Login Activity. */

                Class<?> c = null;
                try {
                    c = Class.forName(activityToStart);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Intent mainIntent = new Intent(SplashScreenActivity.this, c);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}