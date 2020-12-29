package com.example.android.arkanoid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    private MenuActivity menu;
    private Button guestRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        menu = new MenuActivity();
        guestRegisterButton = findViewById(R.id.btnGuestRegister);

        if (menu.isGuestUser == true) {
            guestRegisterButton.setVisibility(View.VISIBLE);
        }
    }

    public void guestRegister(View view) {
    }
}