package com.example.android.arkanoid.Classes;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.arkanoid.Activities.MainActivity;

import java.io.Serializable;

//Fragment that manipulates and update the profile attributes


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

    //update local data with values in the profile object
    public void updateProfile() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        editor.commit();
    }
}
