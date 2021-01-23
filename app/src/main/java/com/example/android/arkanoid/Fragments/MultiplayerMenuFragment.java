package com.example.android.arkanoid.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.Adapters.MatchItemAdapter;
import com.example.android.arkanoid.Classes.OnlineMatch;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class MultiplayerMenuFragment extends Fragment {

    private Profile profile;
    private Button btnSearchOpponent;
    private TextView tvTotalPlayed;
    private TextView tvTotalWin;
    private TextView tvTotalLose;
    private ListView lvMatchItem;
    private FirebaseDatabase database;
    private String userId;
    private String userIdOpponent;
    private String usernameOpponent;
    private OnlineMatch match;
    private ArrayList<OnlineMatch> matchList;
    private long totalWin;
    private long totalLose;
    private long totalPlayed;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiplayer_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();

        profile = (Profile) getArguments().getSerializable("profile");
        userId = profile.getUserId();
        lvMatchItem = getView().findViewById(R.id.lvMatch);
        btnSearchOpponent = getView().findViewById(R.id.btnSearchOpponent);
        tvTotalPlayed = getView().findViewById(R.id.tvTotalPlayed);
        tvTotalWin = getView().findViewById(R.id.tvTotalWin);
        tvTotalLose = getView().findViewById(R.id.tvTotalLose);

        setSearchOpponentListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        ProgressBar progressBar = getView().findViewById(R.id.progressBarMulti);
        progressBar.setVisibility(View.VISIBLE);
        Button btnSearchOpponent  = getView().findViewById(R.id.btnSearchOpponent);
        btnSearchOpponent.setVisibility(View.GONE);
        matchList = new ArrayList<>();

        getAllMatch();
        setAdapterAndTextView();
    }

    private void setSearchOpponentListener() {
        btnSearchOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btnSearchOpponent  = getView().findViewById(R.id.btnSearchOpponent);
                btnSearchOpponent.setVisibility(View.INVISIBLE);
                ProgressBar progressBar = getView().findViewById(R.id.progressBarMulti);
                progressBar.setVisibility(View.VISIBLE);
                createNewMatch();
            }
        });
    }

    private void startGame() {
        Intent intentGame = new Intent(getContext(), GameActivity.class);
        intentGame.putExtra("profile", profile);
        intentGame.putExtra("match", match);
        startActivity(intentGame);
    }

    public void createNewMatch() {
        String id = generateRandomMatchId();
        getRandomOpponent();
        getUsernameDelay(id);
        match = new OnlineMatch(id, profile.getUserName(), usernameOpponent, userId);
        match.setStatus(OnlineMatch.IN_PROGRESS);

    }

    private String generateRandomMatchId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999));
    }

    private void getRandomOpponent() {
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, Object> profiles = (HashMap<String, Object>) dataSnapshot.child("Profiles").getValue();
                Set<String> set = profiles.keySet();
                set.remove(userId);

                getRandomUserId(set);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getRandomUserId(Set<String> set) {
        int size = set.size();
        int item = new Random().nextInt(size);

        int i = 0;
        for(String str : set) {
            if (i == item) {
                userIdOpponent = str;
            }
            i++;
        }
    }
    private void getUsernameFromUserId(String userId) {
        DatabaseReference myRef = database.getReference().child("Profiles").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                usernameOpponent = (String) dataSnapshot.child("UserName").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getUsernameDelay(final String id) {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                getUsernameFromUserId(userIdOpponent);
                uploadMatch(id);
            }
        }, 1700);
    }
    private void uploadMatch(final String id) {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                DatabaseReference myRef = database.getReference("Profiles").child(userId).child("OnlineMatches").child(id);
                myRef.child("Opponent").setValue(usernameOpponent);
                myRef.child("Status").setValue(OnlineMatch.IN_PROGRESS);
                myRef.child("IsCounted").setValue("False");
                DatabaseReference myRef2 = database.getReference("Profiles").child(userIdOpponent).child("OnlineMatches").child(id);
                myRef2.child("Opponent").setValue(profile.getUserName());
                myRef2.child("Status").setValue(OnlineMatch.IN_PROGRESS);
                myRef2.child("IsCounted").setValue("False");
                ProgressBar progressBar = getView().findViewById(R.id.progressBarMulti);
                progressBar.setVisibility(View.GONE);
                Button btnSearchOpponent  = getView().findViewById(R.id.btnSearchOpponent);
                btnSearchOpponent.setVisibility(View.VISIBLE);
                startGame();
            }
        }, 1700);
    }

    private void getAllMatch() {
        DatabaseReference myRef = database.getReference().child("Profiles").child(userId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String id;
                String opponent = "";
                String status = "";

                HashMap<String,String> matchesMap= (HashMap<String,String>) dataSnapshot.child("OnlineMatches").getValue();
                if(matchesMap != null) {
                    Iterator i = matchesMap.entrySet().iterator();
                    while (i.hasNext()) {
                        long[] score = new long[3];
                        long matchCounter = 0;

                        Map.Entry entry = (Map.Entry) i.next();

                        if (entry.getKey().equals("TotalWin"))
                            totalWin = (long) entry.getValue();
                        else if (entry.getKey().equals("TotalLose"))
                            totalLose = (long) entry.getValue();
                        else if (entry.getKey().equals("TotalPlayed"))
                            totalPlayed = (long) entry.getValue();
                        else {
                            id = (String) entry.getKey();

                            HashMap<String, String> hm = (HashMap<String, String>) entry.getValue();
                            Iterator i2 = hm.entrySet().iterator();
                            while (i2.hasNext()) {
                                Map.Entry entry2 = (Map.Entry) i2.next();
                                if (entry2.getKey().equals("Opponent")) {
                                    opponent = (String) entry2.getValue();
                                } else if (entry2.getKey().equals("Status")) {
                                    status = (String) entry2.getValue();
                                } else if (entry2.getKey().equals("Counter")) {
                                    matchCounter = (long) entry2.getValue();
                                } else if (entry2.getKey().equals("Scores")) {
                                    HashMap<String, String> hm2 = (HashMap<String, String>) entry2.getValue();
                                    TreeMap<String, String> tm = new TreeMap<>(hm2);

                                    Iterator i3 = tm.entrySet().iterator();
                                    int counter = 0;
                                    while (i3.hasNext()) {
                                        Map.Entry entry3 = (Map.Entry) i3.next();
                                        score[counter] = (long) entry3.getValue();
                                        ++counter;
                                    }
                                }
                            }
                            OnlineMatch match = new OnlineMatch(id, profile.getUserName(), opponent, userId);
                            match.setStatus(status);
                            match.setCounter((int) matchCounter);
                            getOpponentScores(id, opponent, match);
                            for (int j = 0; j < score.length; j++) {
                                match.setPlayer1Score(score[j], j);
                            }
                            matchList.add(match);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setAdapterAndTextView() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                tvTotalPlayed.setText("Total Played: " + totalPlayed);
                tvTotalWin.setText("Total Win: " + totalWin);
                tvTotalLose.setText("Total Lose: " + totalLose);

                MatchItemAdapter adapter;
                adapter = new MatchItemAdapter(getContext(), profile, matchList);
                lvMatchItem.setAdapter(adapter);
                ProgressBar progressBar = getView().findViewById(R.id.progressBarMulti);
                progressBar.setVisibility(View.GONE);
                Button btnSearchOpponent  = getView().findViewById(R.id.btnSearchOpponent);
                btnSearchOpponent.setVisibility(View.VISIBLE);
            }
        }, 1700);
    }

    private void getOpponentScores(final String id, final String opponent, final OnlineMatch match) {
        DatabaseReference myRef = database.getReference().child("Profiles");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String str = "";
                long[] scores = new long[3];

                HashMap<String, String> hm = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator i = hm.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry entry = (Map.Entry) i.next();
                    HashMap<String, String> hm2 = (HashMap<String, String>) entry.getValue();
                    Iterator i2 = hm2.entrySet().iterator();
                    while (i2.hasNext()) {
                        Map.Entry entry2 = (Map.Entry) i2.next();
                        if (entry2.getKey().equals("UserName") && opponent.equals(entry2.getValue())) 
                            str = (String) entry2.getValue();
                        if (entry2.getKey().equals("OnlineMatches") && str.equals(opponent)) {
                            HashMap<String, String> hm3 = (HashMap<String, String>) entry2.getValue();
                            Iterator i3 = hm3.entrySet().iterator();
                            while (i3.hasNext()) {
                                Map.Entry entry3 = (Map.Entry) i3.next();
                                if (entry3.getKey().equals(id)) {
                                    HashMap<String, String> hm4 = (HashMap<String, String>) entry3.getValue();
                                    Iterator i4 = hm4.entrySet().iterator();
                                    while (i4.hasNext()) {
                                        Map.Entry entry4 = (Map.Entry) i4.next();
                                        if (entry4.getKey().equals("Scores")) {
                                            HashMap<String, String> hm5 = (HashMap<String, String>) entry4.getValue();
                                            TreeMap<String, String> tm = new TreeMap<>(hm5);
                                            Iterator i5 = tm.entrySet().iterator();
                                            int counter = 0;
                                            while (i5.hasNext()) {
                                                Map.Entry entry5 = (Map.Entry) i5.next();
                                                scores[counter] = (long) entry5.getValue();
                                                match.setPlayer2Score(scores[counter], counter);
                                                match.setWinOrLose();
                                                counter++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}