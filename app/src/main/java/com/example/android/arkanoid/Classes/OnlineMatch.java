package com.example.android.arkanoid.Classes;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.android.arkanoid.Activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

//this class contains methods to retrieve, manipulate and manage multiplayer matches information

public class OnlineMatch implements Serializable {

    static public final String WIN = "Win";
    static public final String LOSE = "Lose";
    static public final String IN_PROGRESS = "In Progress";
    static public final long GAME_NOT_PLAYED = -9999;
    private final String player1;
    private final String player2;
    private final long[] player1Score = new long[3];
    private final long[] player2Score = new long[3];
    private final String id;
    private int counter;
    private final String userId;
    private String status;
    private long totalPlayed;
    private long totalWin;
    private long totalLose;
    private String isCounted;
    private Profile profile;

    public OnlineMatch(String id, String player1, String player2, String userId, Profile profile) {
        this.profile = profile;
        this.player1 = player1;
        this.player2 = player2;
        this.id = id;
        this.userId = userId;
        player1Score[0] = 0;
        player1Score[1] = 0;
        player1Score[2] = 0;
        player2Score[0] = GAME_NOT_PLAYED;
        player2Score[1] = GAME_NOT_PLAYED;
        player2Score[2] = GAME_NOT_PLAYED;
        counter = 0;
    }

    public long[] getPlayer1Score() {
        return player1Score;
    }

    public long[] getPlayer2Score() {
        return player2Score;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getId() {
        return id;
    }

    public void setPlayer1Score(long score, int i) {
        this.player1Score[i] = score;
    }

    public void setPlayer2Score(long score, int i) {
        this.player2Score[i] = score;
    }

    public void increaseCounter() {
        ++counter;
    }

    public int getCounter() {
        return counter;
    }

    //update online matches information on database
    public void updateMatch() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles").child(userId).child("OnlineMatches").child(id);
        myRef.child("Scores").child("Score " + counter).setValue(player1Score[counter - 1]);
        myRef.child("Status").setValue(status);
        myRef.child("Counter").setValue(counter);
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getStatus() {
        return status;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    //check match scores to define who won and who lose
    public void setWinOrLose() {
        if (counter == 3 && player2Score[2] != -9999) {

            int player1winCounter = 0;
            int player2winCounter = 0;

            for (int i = 0; i < 3; i++) {
                if (player1Score[i] >= player2Score[i])
                    player1winCounter++;
                else
                    player2winCounter++;
            }
            if (player1winCounter > player2winCounter) {
                setStatus(WIN);
                getAndSetWinLoseCounter(WIN);
                updateMatchQuest();
            }
            else {
                setStatus(LOSE);
                getAndSetWinLoseCounter(LOSE);
            }
            updateMatch();
        }
    }

    //get information about total matches from database
    private void getAndSetWinLoseCounter(final String str) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Profiles").child(userId).child("OnlineMatches");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isCounted = (String) dataSnapshot.child(id).child("IsCounted").getValue();
                totalPlayed = (long) dataSnapshot.child("TotalPlayed").getValue();
                totalWin = (long) dataSnapshot.child("TotalWin").getValue();
                totalLose = (long) dataSnapshot.child("TotalLose").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        delay(str, myRef);
    }

    //update database information about total matches
    private void updateWinLoseCounter(String str, DatabaseReference myRef) {

        if (isCounted.equals("False")) {
            myRef.child("TotalPlayed").setValue(++totalPlayed);

            if (str.equals(WIN)) {
                myRef.child("TotalWin").setValue(++totalWin);
            } else if (str.equals(LOSE)) {
                myRef.child("TotalLose").setValue(++totalLose);
            }

            myRef.child(id).child("IsCounted").setValue("True");
        }
    }

    //set a delay to give firebase the time to update data
    private void delay(final String str, final DatabaseReference myRef) {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                updateWinLoseCounter(str, myRef);
            }
        }, 1500);
    }

    //increase progress for the multiplayer quest
    private void updateMatchQuest() {
        int progress = profile.getQuestsList().get(Quest.QUEST_WIN_50_MULTIPLAYER).getProgress();
        profile.getQuestsList().get(Quest.QUEST_WIN_50_MULTIPLAYER).setProgress(++progress);
        Services services = new Services(profile.getUserId());
        services.updateQuestsFile(MainActivity.context, profile.getQuestsList());
    }
}
