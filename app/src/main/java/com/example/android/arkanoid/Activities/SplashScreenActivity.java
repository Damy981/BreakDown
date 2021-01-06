package com.example.android.arkanoid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private String activityToStart;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    // Called when the activity is first created
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        SharedPreferences preferences = getSharedPreferences(Services.SHARED_PREF_DIR, MODE_PRIVATE);
        // Check if user is signed in with an active account (not-null) and update the string with the right class name.
        if(user != null)
            if (user.isEmailVerified())
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            else
                activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";

        else
            if (preferences.getString("userName", null) != null)
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            else
               activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";


        /* Handler to start the Menu-Activity
           and close this Splash-Screen after some seconds.*/
        // Duration of wait
        int SPLASH_DISPLAY_LENGTH = 1000;
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