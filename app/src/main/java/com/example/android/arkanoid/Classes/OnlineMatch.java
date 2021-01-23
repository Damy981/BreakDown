package com.example.android.arkanoid.Classes;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class OnlineMatch implements Serializable {

    static public final String WIN = "Win";
    static public final String LOSE = "Lose";
    static public final String IN_PROGRESS = "In Progress";
    static public final long GAME_NOT_PLAYED = -9999;
    private String player1;
    private String player2;
    private long player1Score[] = new long[3];
    private long player2Score[] = new long[3];
    private String id;
    private int counter;
    private String userId;
    private String status;

    public OnlineMatch(String id, String player1, String player2, String userId) {
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

    public void setWinOrLose() {
        Log.i("cacca", String.valueOf(player2Score[2]));

        if (counter == 3 && player2Score[2] != -9999) {

            int player1winCounter = 0;
            int player2winCounter = 0;

            for (int i = 0; i < 3; i++) {
                if (player1Score[i] >= player2Score[i])
                    player1winCounter++;
                else
                    player2winCounter++;
            }
            if (player1winCounter > player2winCounter)
                setStatus(WIN);
            else
                setStatus(LOSE);

            updateMatch();
        }
    }
}
