package com.example.android.arkanoid;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile {

    private int levelNumber;
    private int coins;
    private String userName;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userId;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Profile(){
       initializeDatabase();
       setProfile(myRef);
    }

    public void increaseLevel() {
        Log.i("cacca", "2 - " + String.valueOf(levelNumber));
        myRef.child("LevelNumber").setValue(++levelNumber);
        Log.i("cacca", "3 - " + String.valueOf(levelNumber));
    }

    public void setProfile(DatabaseReference myRef) {
        initializeDatabase();
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                levelNumber = dataSnapshot.child("LevelNumber").getValue(int.class);
                Log.i("cacca", "1 - " + String.valueOf(levelNumber));
                coins = dataSnapshot.child("Coins").getValue(int.class);
                userName = dataSnapshot.child("UserName").getValue(String.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeDatabase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if (user != null) {
            userId = user.getUid();
            myRef = database.getReference("Profiles").child(userId);

        }
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public FirebaseUser getUser() {
        return user;
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
}
