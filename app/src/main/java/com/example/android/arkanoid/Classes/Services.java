package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
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
        populateQuestList();
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

    //add the quests text to the arraylist which will be write on file with createQuestsFiles
    private void populateQuestList() {
        questsList.add(new Quest("Destroy 100 bricks", 0, 100, 20, true));
        questsList.add(new Quest("Win 5 games", 0, 5, 20, true));
        questsList.add(new Quest("Win 3 games without losing lives", 0, 3, 20, true));
        questsList.add(new Quest("Destroy 10,000 bricks", 0, 10000, 250, false));
        questsList.add(new Quest("Win 50 games in multiplayer mode", 0, 50, 250, false));
        questsList.add(new Quest("Create a level", 0, 1, 30, false));
        questsList.add(new Quest("Defuse 50 nitros", 0, 50, 250, false));
    }

    public boolean getMusicSetting() {
        return preferences.getBoolean("tbMusicStatus", true);
    }

    public boolean getAccelerometerSetting() {
        return preferences.getBoolean("tbAccelStatus", false);
    }

    public void uploadQuestsFile(StorageReference storageRef, Context context) {
        Uri file = Uri.fromFile(new File(context.getFilesDir()+ "/" +questsFileName));
        StorageReference questsRef = storageRef.child("file/"+ questsFileName);
        questsRef.putFile(file);
    }

    public void downloadQuestsFile(StorageReference questsRef, Context context) {
        File localQuestsFile = null;
        localQuestsFile = new File(context.getFilesDir(), questsFileName);
        Log.i("Path temp", localQuestsFile.getAbsolutePath());
        questsRef.getFile(localQuestsFile)
        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.i("Download", "completato");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("Download", "Non completato");
            }
        });
    }

    public String getQuestsFileName() {
        return questsFileName;
    }

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

    public void initializeOnlineMatches() {
        String userId = preferences.getString("userId", null);

        myRef.child(userId).child("OnlineMatches").child("TotalPlayed").setValue(0);
        myRef.child(userId).child("OnlineMatches").child("TotalWin").setValue(0);
        myRef.child(userId).child("OnlineMatches").child("TotalLose").setValue(0);
    }
}
