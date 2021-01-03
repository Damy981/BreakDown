package com.example.android.arkanoid.Classes;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Profile implements Serializable {

    private int levelNumber;
    private int coins;
    private String userName;
    private boolean useAccelerometer;
    private String userId;


    public Profile(int levelNumber, int coins, String userName, boolean b, String userId) {
        this.userName = userName;
        this.levelNumber = levelNumber;
        this.coins = coins;
        useAccelerometer = b;
        this.userId = userId;
    }

    public int getCoins() {
        return coins;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setAccelerometer(boolean b) {
        useAccelerometer = b;
    }

    public boolean isUsedAccelerometer() {
        return useAccelerometer;
    }

    public void increaseLevel() {
        levelNumber++;
    }

    public String getUserId () {
        return  userId;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void uploadProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles").child(userId);
        myRef.child("Coins").setValue(coins);
        myRef.child("LevelNumber").setValue(levelNumber);
    }


}
