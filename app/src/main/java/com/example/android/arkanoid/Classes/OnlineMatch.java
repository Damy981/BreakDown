package com.example.android.arkanoid.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class OnlineMatch implements Serializable {

    private String player1;
    private String player2;
    private long score[] = new long[3];
    private String id;
    private int counter;
    private String userId;

    public OnlineMatch(String id, String player1, String player2, String userId) {
        this.player1 = player1;
        this.player2 = player2;
        this.id = id;
        this.userId = userId;
        score[0] = 0;
        score[1] = 0;
        score[2] = 0;
        counter = 0;
    }

    public long[] getScore() {
        return score;
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

    public void setScore(long score, int i) {
        this.score[i] = score;
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
        myRef.child("Scores").child("Score " + counter).setValue(score[counter - 1]);
    }
}
