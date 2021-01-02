package com.example.android.arkanoid.Classes;


import java.io.Serializable;

public class Profile implements Serializable {

    private int levelNumber;
    private int coins;
    private String userName;
    private boolean useAccelerometer;

    public Profile(int levelNumber, int coins, String userName, boolean b) {
        this.userName = userName;
        this.levelNumber = levelNumber;
        this.coins = coins;
        useAccelerometer = b;
    }


    public void increaseLevel() {
        //myRef.child("LevelNumber").setValue(++levelNumber);
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
}
