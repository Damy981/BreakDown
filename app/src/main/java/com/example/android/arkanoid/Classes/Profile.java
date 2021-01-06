package com.example.android.arkanoid.Classes;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.arkanoid.Activities.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;

//Fragment that manipulates and update the profile attributes


public class Profile implements Serializable {

    private int levelNumber;
    private int coins;
    private String userName;
    private boolean useAccelerometer;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private String[] items = {"Item 1", "Item 2 ", "Item 3 ", "Item 4 ", "Item 5 "};
    private String userId;
    private String prices;
    private String quantities;

    public Profile(int levelNumber, int coins, String userName, boolean b, String userId, String prices, String quantities) {
        this.userName = userName;
        this.levelNumber = levelNumber;
        this.coins = coins;
        useAccelerometer = b;
        this.userId = userId;
        this.prices = prices;
        this.quantities = quantities;
        int[] p = retrieveValuesFromString(prices);
        int[] q = retrieveValuesFromString(quantities);
        for(int i = 0; i < items.length; i++){
            PowerUp pU = new PowerUp(items[i], p[i], q[i]);
            powerUps.add(pU);
        }
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

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
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

    public void setPrices(int price, int i) {
        powerUps.get(i).setPrice(price);
        int[] array = new int[items.length];
        prices = putValuesIntoString(getPriceArray(array));
    }

    //update local data with values in the profile object
    public void updateProfile() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        int[] array = new int[items.length];
        prices = putValuesIntoString(getPriceArray(array));
        editor.putString("prices", prices);
        quantities = putValuesIntoString(getQuantitiesArray(array));
        editor.putString("quantities", quantities);
        Log.i("update", prices + " " + quantities);
        editor.commit();
    }

    /**/
    private int[] retrieveValuesFromString(String string) {
        String[] strArray = string.split(",");
        int[] intArray = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }

    /**/
    private String putValuesIntoString(int[] array) {
        StringBuilder sb = new StringBuilder();
        String string = "";
        for(int i = 0; i < array.length; i++) {
            if(i < (array.length - 1)){
                string = sb.append(array[i]+",").toString();
            }
            else {
                string = sb.append(array[i]).toString();
            }
        }
        return string;
    }

    private int[] getPriceArray(int[] array) {
        for (int j = 0; j < items.length; j++){
            array[j] = powerUps.get(j).getPrice();
        }
        return array;
    }

    private int[] getQuantitiesArray(int[] array) {
        for (int j = 0; j < items.length; j++){
            array[j] = powerUps.get(j).getQuantity();
        }
        return array;
    }
}
