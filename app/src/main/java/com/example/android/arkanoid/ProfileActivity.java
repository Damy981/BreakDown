package com.example.android.arkanoid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button guestRegisterButton;
    private FirebaseUser user;
    private TextView tvUsername;
    private TextView tvCoins;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        guestRegisterButton = findViewById(R.id.btnGuestRegister);
        tvUsername = findViewById(R.id.tvUsername);
        tvCoins = findViewById(R.id.tvCoins);

        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        tvUsername.setText(profile.getUserName());
        tvCoins.setText(String.valueOf(profile.getCoins()));

        if (user.isAnonymous()) {
            guestRegisterButton.setVisibility(View.VISIBLE);
        }
    }

    public void guestRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}