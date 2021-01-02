package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Fragments.ProfileFragment;
import com.example.android.arkanoid.Fragments.QuestFragment;
import com.example.android.arkanoid.R;
import com.example.android.arkanoid.Fragments.RankingFragment;
import com.example.android.arkanoid.Fragments.SettingsFragment;
import com.example.android.arkanoid.Fragments.ShopFragment;
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
    private FragmentManager fm;
    private FragmentTransaction tx;
    private Fragment fragment;
    private ConstraintLayout menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        menu = findViewById(R.id.menu);

        fm = getSupportFragmentManager();
        retrieveProfileData();  //fare in modo che si chiama ogni volta che si passa dal menu (forse onResume)
    }


    @Override
    public void onBackPressed() {
        if (menu.getVisibility() == View.VISIBLE)
            moveTaskToBack(true);
        else
            changeVisibility();
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void btnProfileClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile", profile);
        fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void btnShopClick(View view) {
        fragment = new ShopFragment();
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void btnQuestClick(View view) {
        fragment = new QuestFragment();
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void btnLevelEditorClick(View view) {
        Intent intentLevelEditor = new Intent(this, LevelEditorActivity.class);
        startActivity(intentLevelEditor);
    }

    public void btnRankingClick(View view) {
        fragment = new RankingFragment();
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void btnSettingsClick(View view) {
        fragment = new SettingsFragment();
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
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

    private void changeVisibility() {
        if (menu.getVisibility() == View.VISIBLE)
            menu.setVisibility(View.GONE);
        else {
            tx = fm.beginTransaction();
            tx.remove(fragment).commit();
            menu.setVisibility(View.VISIBLE);
        }
    }
}