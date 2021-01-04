package com.example.android.arkanoid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

    private final int LOADING_TIME = 1200;
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
    private Bundle bundle;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        menu = findViewById(R.id.menu);

        fm = getSupportFragmentManager();
        retrieveProfileData();
        bundle = new Bundle();
        preferences = getPreferences(MODE_PRIVATE);
        loadingScreen();
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
        bundle.putSerializable("profile", profile);
        fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void btnShopClick(View view) {
        bundle.putSerializable("profile", profile);
        fragment = new ShopFragment();
        fragment.setArguments(bundle);
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
        bundle.putSerializable("profile", profile);
        fragment = new SettingsFragment();
        fragment.setArguments(bundle);
        changeVisibility();
        tx = fm.beginTransaction();
        tx.add(R.id.fragment_place, fragment);
        tx.commit();
    }

    public void startGame(View view) {
        Intent intentGame = new Intent(this, GameActivity.class);
        intentGame.putExtra("profile", profile);
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
                profile = new Profile(levelNumber, coins, userName, preferences.getBoolean("tbAccelStatus", false), userId);
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

    private void loadingScreen() {
        findViewById(R.id.menu).setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(!isNetworkAvailable())
                    showInternetAlert();

                findViewById(R.id.loadingBar).setVisibility(View.GONE);
                findViewById(R.id.menu).setVisibility(View.VISIBLE);
            }
        }, LOADING_TIME);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void showInternetAlert () {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No internet connection available. Please check your connection and try again.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAndRemoveTask();
                        moveTaskToBack(true);
                    }
                });
        alert.show();
    }
}