package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/* Class with functions that manages data in the database and local data
 */

public class Services {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    public static final String SHARED_PREF_DIR = "com.example.android.arkanoid_preferences";
    private ArrayList<String> questsList = new ArrayList();
    public String questsFileName = "quest.txt";


    public Services() {

    }

    public Services(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    //set local data with parameters sent
    public void setSharedPreferences(String username, int coins, int levelNumber, String userId, String prices, String quantities) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        if (userId != null)
            editor.putString("userId", userId);
        editor.putString("prices", prices);
        editor.putString("quantities", quantities);
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
        myRef.child(userId).child("Prices").setValue(preferences.getString("prices", "5,5,5,5,5"));
        myRef.child(userId).child("Quantities").setValue(preferences.getString("quantities", "0,0,0,0,0"));

    }

    //use the local data to create a profile object which is returned
    public Profile buildProfile() {
        int coins = preferences.getInt("coins", 0);
        int levelNumber = preferences.getInt("levelNumber", 1);
        String userName = preferences.getString("userName", "GuestUser");
        String prices = preferences.getString("prices", "5,5,5,5,5");
        String quantities = preferences.getString("quantities", "0,0,0,0,0");
        Profile profile = new Profile(levelNumber, coins, userName, preferences.getBoolean("tbAccelStatus", false), null, prices, quantities);
        return profile;
    }

    //set only username and userId in the local data, used when guest want to register
    public void setNameAndUserId(String username, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putString("userId" , userId);
        editor.commit();
    }

    //create the file with all the possible quests from which user quests will be generated
    public void createQuestsFiles(Context context) {
        populateQuestList();
        try {
            FileOutputStream questFile = context.openFileOutput(questsFileName, context.MODE_PRIVATE);
            //Log.i("fileDir", String.valueOf(context.getFilesDir()));
            try {
                ObjectOutputStream a = new ObjectOutputStream(questFile);
                a.writeObject(questsList);
                questFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //add the quests text to the arraylist which will be write on file with createQuestsFiles
    private void populateQuestList() {
        questsList.add("Quest 1");
        questsList.add("Quest 2");
        questsList.add("Quest 3");
        questsList.add("Quest 4");
        questsList.add("Quest 5");
        questsList.add("Quest 6");
    }
}
