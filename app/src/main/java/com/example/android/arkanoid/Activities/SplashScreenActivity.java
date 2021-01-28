package com.example.android.arkanoid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private String activityToStart;
    static public HashMap<String,String> rankingMap;
    // Called when the activity is first created
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences preferences = getSharedPreferences(Services.SHARED_PREF_DIR, MODE_PRIVATE);
        // Check if user is signed in with an active account (not-null) and update the string with the right class name.
        if(user != null) {
            if (user.isEmailVerified())
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            else
                activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";
        }

        else {
            if (preferences.getString("userName", null) != null)
                activityToStart = "com.example.android.arkanoid.Activities.MenuActivity";
            else
                activityToStart = "com.example.android.arkanoid.Activities.LoginActivity";
        }

        if (isNetworkAvailable())
            getRankingFromDatabase();

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

    private void getRankingFromDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                rankingMap = (HashMap<String,String>) dataSnapshot.child("Ranking").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //return true if internet connection is available else return false
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}