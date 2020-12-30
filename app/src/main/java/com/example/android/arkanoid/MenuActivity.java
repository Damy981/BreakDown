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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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

    public void btnProfileClick(View view) {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
    }

    public void btnShopClick(View view) {
        Intent intentShop = new Intent(this, ShopActivity.class);
        startActivity(intentShop);
    }

    public void btnQuestClick(View view) {
        Intent intentQuest = new Intent(this,QuestActivity.class);
        startActivity(intentQuest);
    }

    public void btnLevelEditorClick(View view) {
        Intent intentLevelEditor = new Intent(this, LevelEditorActivity.class);
        startActivity(intentLevelEditor);
    }

    public void btnRankingClick(View view) {
        Intent intentRanking = new Intent(this,RankingActivity.class);
        startActivity(intentRanking);
    }

    public void btnSettingsClick(View view) {
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }
}