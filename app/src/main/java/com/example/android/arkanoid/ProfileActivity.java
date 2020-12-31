package com.example.android.arkanoid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button guestRegisterButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private int levelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();

        }
        guestRegisterButton = findViewById(R.id.btnGuestRegister);

        if (user.isAnonymous()) {
            guestRegisterButton.setVisibility(View.VISIBLE);
        }
    }

    public void guestRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void increaseLevel() {
        levelNumber++;
    }
}