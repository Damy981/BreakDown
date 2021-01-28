package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.android.arkanoid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/* Class with functions that manages data in the database and local data
 */

public class Services {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    public static final String SHARED_PREF_DIR = "com.example.android.arkanoid_preferences";
    private ArrayList<Quest> questsList = new ArrayList();
    private String questsFileName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Services(String userId) {
        questsFileName = "quest_" + userId + ".bin";
    }

    public Services(SharedPreferences preferences, String userId) {
        this.preferences = preferences;
        questsFileName = "quest_" + userId + ".bin";
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Profiles");
    }

    //set local data with parameters sent
    public void setSharedPreferences(String username, int coins, int levelNumber, String userId, String prices, String quantities, int bestScore) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putInt("coins", coins);
        editor.putInt("levelNumber", levelNumber);
        if (userId != null)
            editor.putString("userId", userId);
        editor.putString("prices", prices);
        editor.putString("quantities", quantities);
        editor.putInt("bestScore", bestScore);
        editor.commit();
    }

    //upload the local data in the database using the userId as a key
    public void updateDatabase() {
        String userId = preferences.getString("userId", null);

        myRef.child(userId).child("LevelNumber").setValue(preferences.getInt("levelNumber", 1));
        myRef.child(userId).child("Coins").setValue(preferences.getInt("coins", 0));
        myRef.child(userId).child("UserName").setValue(preferences.getString("userName", "GuestUser"));
        myRef.child(userId).child("Prices").setValue(preferences.getString("prices", "5,5,5,5,5"));
        myRef.child(userId).child("Quantities").setValue(preferences.getString("quantities", "0,0,0,0,0"));
        myRef.child(userId).child("BestScore").setValue(preferences.getInt("bestScore", 0));

        if(userId != null) {
            DatabaseReference myRef2 = database.getReference("Ranking");
            myRef2.child(preferences.getString("userName", "GuestUser")).setValue(preferences.getInt("bestScore", 0));
        }
    }

    //use the local data to create a profile object which is returned
    public Profile buildProfile(Context context) {
        int coins = preferences.getInt("coins", 0);
        int levelNumber = preferences.getInt("levelNumber", 1);
        String userName = preferences.getString("userName", "GuestUser");
        String prices = preferences.getString("prices", "5,5,5,5,5");
        String quantities = preferences.getString("quantities", "0,0,0,0,0");
        int bestScore = preferences.getInt("bestScore", 0);
        String userId = preferences.getString("userId", null);
        questsList = getQuestListFromFile(context);
        return new Profile(levelNumber, coins, userName, userId, prices, quantities, bestScore, questsList);
    }

    //set only username and userId in the local data, used when guest want to register
    public void setNameAndUserId(String username, String userId) {
        editor = preferences.edit();
        editor.putString("userName" , username);
        editor.putString("userId" , userId);
        editor.commit();
    }

    //create the file with the user quests
    public void createQuestsFile(Context context) {
        populateQuestList(context);
        try {
            FileOutputStream questFile = new FileOutputStream(context.getFilesDir() + "/"+questsFileName);
            try {
                ObjectOutputStream a = new ObjectOutputStream(questFile);
                for(int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++){
                    a.writeObject(questsList.get(i));
                }
                a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //add the quests objects to the arraylist which will be written on file with createQuestsFile
    private void populateQuestList(Context context) {
        questsList.add(new Quest(context.getString(R.string.quest1), 0, 100, 20, true));
        questsList.add(new Quest(context.getString(R.string.quest2), 0, 5, 20, true));
        questsList.add(new Quest(context.getString(R.string.quest3), 0, 3, 20, true));
        questsList.add(new Quest(context.getString(R.string.quest4), 0, 10000, 250, false));
        questsList.add(new Quest(context.getString(R.string.quest5), 0, 50, 250, false));
        questsList.add(new Quest(context.getString(R.string.quest6), 0, 1, 30, false));
        questsList.add(new Quest(context.getString(R.string.quest7), 0, 50, 250, false));
    }

    public boolean getMusicSetting() {
        return preferences.getBoolean("tbMusicStatus", true);
    }

    public boolean getAccelerometerSetting() {
        return preferences.getBoolean("tbAccelStatus", false);
    }

    //upload quests file to firebase storage
    public void uploadQuestsFile(StorageReference storageRef, Context context) {
        Uri file = Uri.fromFile(new File(context.getFilesDir()+ "/" +questsFileName));
        StorageReference questsRef = storageRef.child("file/"+ questsFileName);
        questsRef.putFile(file);
    }

    //download quests file from firebase storage
    public void downloadQuestsFile(StorageReference questsRef, Context context) {
        File localQuestsFile = null;
        localQuestsFile = new File(context.getFilesDir(), questsFileName);
        Log.i("Path temp", localQuestsFile.getAbsolutePath());
        questsRef.getFile(localQuestsFile);
    }

    public String getQuestsFileName() {
        return questsFileName;
    }

    //locally update the quests file by re-writing the given arraylist
    public void updateQuestsFile(Context context, ArrayList<Quest> questsList) {
        try {
            FileOutputStream questFile = new FileOutputStream(context.getFilesDir() + "/"+questsFileName);
            try {
                ObjectOutputStream a = new ObjectOutputStream(questFile);
                for(int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++){
                    a.writeObject(questsList.get(i));
                }
                a.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //retrieve quests information from file
    public ArrayList<Quest> getQuestListFromFile(Context context) {
        ArrayList<Quest> questsList = new ArrayList<>();
        FileInputStream questFile;
        try {
            questFile = new FileInputStream(context.getFilesDir() + "/" + getQuestsFileName());
            ObjectInputStream q = new ObjectInputStream(questFile);
            for(int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++){
                questsList.add((Quest) q.readObject());
            }
            q.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return questsList;
    }

    //initialize online matches information on database
    public void initializeOnlineMatches() {
        String userId = preferences.getString("userId", null);

        myRef.child(userId).child("OnlineMatches").child("TotalPlayed").setValue(0);
        myRef.child(userId).child("OnlineMatches").child("TotalWin").setValue(0);
        myRef.child(userId).child("OnlineMatches").child("TotalLose").setValue(0);
    }
}
