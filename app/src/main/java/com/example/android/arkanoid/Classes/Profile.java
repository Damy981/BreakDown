package com.example.android.arkanoid.Classes;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.arkanoid.Activities.MainActivity;
import com.example.android.arkanoid.R;

import java.io.Serializable;
import java.util.ArrayList;

//Fragment that manipulates and update the profile attributes


public class Profile implements Serializable {

    static public final int STATS_NUMBER = 2;
    private int levelNumber;
    private int coins;
    private String userName;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private String[] items = {"Coins drop rate", "Paddle length", "Freeze", "Explosive Ball", "Item 5 "};
    private String userId;
    private String prices;
    private String quantities;
    private int bestScore;

    public Profile(int levelNumber, int coins, String userName, String userId, String prices, String quantities, int bestScore) {
        this.userName = userName;
        this.levelNumber = levelNumber;
        this.coins = coins;
        this.userId = userId;
        this.bestScore = bestScore;
        //SharedPreferences cannot store arrays, so numerical data for owned power-ups are stored in String arrays
        this.prices = prices;
        this.quantities = quantities;
        /*numerical values are extracted from Strings and used to build the arraylist
        that contains all the information for the power-ups owned by the user*/
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

    public String getQuantities() {
        return quantities;
    }

    public int getBestScore() {
        return bestScore;
    }

    //put the price for each power-up in an int array
    private int[] getPriceArray(int[] array) {
        for (int j = 0; j < items.length; j++){
            array[j] = powerUps.get(j).getPrice();
        }
        return array;
    }

    //put the quantity for each power-up in an int array
    private int[] getQuantitiesArray(int[] array) {
        for (int j = 0; j < items.length; j++){
            array[j] = powerUps.get(j).getQuantity();
        }
        return array;
    }

    public String getUserId () {
        return  userId;
    }

    public void increaseLevel() {
        levelNumber++;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    //modify the price of the power-up in the specified position and set the string that contains all the prices
    public void setPrices(int price, int i) {
        powerUps.get(i).setPrice(price);
        int[] array = new int[items.length];
        prices = putValuesIntoString(getPriceArray(array));
    }

    //modify the quantity of the power-up in the specified position and set the string that contains all the quantities
    public void setQuantities(int quantity, int i) {
        powerUps.get(i).setQuantity(quantity);
        int[] array = new int[items.length];
        prices = putValuesIntoString(getQuantitiesArray(array));
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
        editor.putInt("bestScore", bestScore);
        editor.commit();
    }

    //get a string of numerical characters separated by a comma, and return an array of int values
    private int[] retrieveValuesFromString(String string) {
        String[] strArray = string.split(",");
        int[] intArray = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }

    //put the values of a int array in a string, dividing each number by a comma, and return that string
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

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
}
