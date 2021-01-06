package com.example.android.arkanoid.Classes;

import android.content.SharedPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Class with functions that manages data in the database and local data
 */

public class Services {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    public static final String SHARED_PREF_DIR = "com.example.android.arkanoid_preferences";

    public Services(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    //set local data with parameters sent
    public void setSharedPreferences(String username, int coins, int levelNumber, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        if (userId != null)
            editor.putString("userId", userId);
        editor.commit();
    }

    //upload the local data in the database using the userId as a key
    public void updateDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles");
        String userId = preferences.getString("userId", null);

        myRef.child(userId).child("LevelNumber").setValue(preferences.getInt("levelNumber", 1));
        myRef.child(userId).child("Coins").setValue(preferences.getInt("coins", 0));
        myRef.child(userId).child("UserName").setValue(preferences.getString("userName", "GuestUser"));
        //aggiungere i power up
    }

    //use the local data to create a profile object which is returned
    public Profile buildProfile() {
        int coins = preferences.getInt("coins", 0);
        int levelNumber = preferences.getInt("levelNumber", 1);
        String userName = preferences.getString("userName", "GuestUser");
        Profile profile = new Profile(levelNumber, coins, userName, preferences.getBoolean("tbAccelStatus", false), null);
        return profile;
    }

    //set only username and userId in the local data, used when guest want to register
    public void setNameAndUserId(String username, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putString("userId" , userId);
        editor.commit();
    }
}
