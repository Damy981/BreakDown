package com.example.android.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        retrieveProfileData();  //fare in modo che si chiama ogni volta che si passa dal menu (forse onResume)
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
        intentProfile.putExtra("profile", profile);
        startActivity(intentProfile);
    }

    public void btnShopClick(View view) {
        Intent intentShop = new Intent(this, ShopActivity.class);
        startActivity(intentShop);
    }

    public void btnQuestClick(View view) {
        Intent intentQuest = new Intent(this, QuestActivity.class);
        startActivity(intentQuest);
    }

    public void btnLevelEditorClick(View view) {
        Intent intentLevelEditor = new Intent(this, LevelEditorActivity.class);
        startActivity(intentLevelEditor);
    }

    public void btnRankingClick(View view) {
        Intent intentRanking = new Intent(this, RankingActivity.class);
        startActivity(intentRanking);
    }

    public void btnSettingsClick(View view) {
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    public void startGame(View view) {
        Intent intentGame = new Intent(this, GameActivity.class);
        startActivity(intentGame);
    }

    private void retrieveProfileData() {
        database = FirebaseDatabase.getInstance();

        if (user != null) {
            userId = user.getUid();
            myRef = database.getReference("Profiles").child(userId);

        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int levelNumber = dataSnapshot.child("LevelNumber").getValue(int.class);
                int coins = dataSnapshot.child("Coins").getValue(int.class);
                String userName = dataSnapshot.child("UserName").getValue(String.class);
                profile = new Profile(levelNumber, coins, userName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}