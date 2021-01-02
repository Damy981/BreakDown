package com.example.android.arkanoid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.example.android.arkanoid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private FirebaseAuth mAuth;
    private String activityToStart;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in with an active account (non-null) and update the string with the right class name.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isAnonymous()) {
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            } else if (currentUser.isEmailVerified()) {
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            }
            else
                activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";
        }
        else {
            activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";
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