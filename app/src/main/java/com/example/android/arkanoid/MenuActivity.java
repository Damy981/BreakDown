package com.example.android.arkanoid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    protected boolean isGuestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user.isAnonymous())
            isGuestUser = true;
        else
            isGuestUser = false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}