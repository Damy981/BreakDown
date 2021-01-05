package com.example.android.arkanoid.Classes;

import android.content.SharedPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Services {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public Services(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public void setSharedPreferences(String username, int coins, int levelNumber, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        if (userId != null)
            editor.putString("userId", userId);
        editor.commit();
    }

    public void updateDatabase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles");
        String userId = preferences.getString("userId", null);

        myRef.child(userId).child("LevelNumber").setValue(preferences.getInt("levelNumber", 1));
        myRef.child(userId).child("Coins").setValue(preferences.getInt("coins", 0));
        myRef.child(userId).child("UserName").setValue(preferences.getString("userName", "GuestUser"));
        //aggiungere i power up
    }

    public Profile buildProfile() {
        int coins = preferences.getInt("coins", 0);
        int levelNumber = preferences.getInt("levelNumber", 1);
        String userName = preferences.getString("userName", "GuestUser");
        Profile profile = new Profile(levelNumber, coins, userName, preferences.getBoolean("tbAccelStatus", false), null);
        return profile;
    }

    public void setNameAndUserId(String username, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putString("userId" , userId);
        editor.commit();
    }

}
