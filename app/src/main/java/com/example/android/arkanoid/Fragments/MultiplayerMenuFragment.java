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
import android.widget.ImageView;
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

        ImageView btnBackMultiplayer = getView().findViewById(R.id.ivBackMultiplayer);
        btnBackMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

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
    //create new match with random opponent and set match in progress
    public void createNewMatch() {
        String id = generateRandomMatchId();
        getRandomOpponent();
        getUsernameDelay(id);
        match = new OnlineMatch(id, profile.getUserName(), usernameOpponent, userId, profile);
        match.setStatus(OnlineMatch.IN_PROGRESS);
    }

    private String generateRandomMatchId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1, 99999));
    }

    //get a set of all users id from database (excluding logged userid)
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

    //get a random userid from user id set
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

    //get username corresponding to given userid
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

    //upload match on the database and start the game
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

    //get all matches data from database and add them in the match arraylist
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

                HashMap<String,String> userMatchesInformationMap= (HashMap<String,String>) dataSnapshot.child("OnlineMatches").getValue();
                if(userMatchesInformationMap != null) {
                    Iterator i = userMatchesInformationMap.entrySet().iterator();
                    while (i.hasNext()) {
                        long[] score = new long[3];
                        long matchCounter = 0;

                        Map.Entry userMatchesInformationEntry = (Map.Entry) i.next();

                        if (userMatchesInformationEntry.getKey().equals("TotalWin"))
                            totalWin = (long) userMatchesInformationEntry.getValue();
                        else if (userMatchesInformationEntry.getKey().equals("TotalLose"))
                            totalLose = (long) userMatchesInformationEntry.getValue();
                        else if (userMatchesInformationEntry.getKey().equals("TotalPlayed"))
                            totalPlayed = (long) userMatchesInformationEntry.getValue();
                        else {
                            id = (String) userMatchesInformationEntry.getKey();

                            HashMap<String, String> matchesMap = (HashMap<String, String>) userMatchesInformationEntry.getValue();
                            Iterator i2 = matchesMap.entrySet().iterator();
                            while (i2.hasNext()) {
                                Map.Entry matchEntry = (Map.Entry) i2.next();
                                if (matchEntry.getKey().equals("Opponent")) {
                                    opponent = (String) matchEntry.getValue();
                                } else if (matchEntry.getKey().equals("Status")) {
                                    status = (String) matchEntry.getValue();
                                } else if (matchEntry.getKey().equals("Counter")) {
                                    matchCounter = (long) matchEntry.getValue();
                                } else if (matchEntry.getKey().equals("Scores")) {
                                    HashMap<String, String> scoresMap = (HashMap<String, String>) matchEntry.getValue();
                                    TreeMap<String, String> sortedScoresMap = new TreeMap<>(scoresMap);

                                    Iterator i3 = sortedScoresMap.entrySet().iterator();
                                    int counter = 0;
                                    while (i3.hasNext()) {
                                        Map.Entry scoreEntry = (Map.Entry) i3.next();
                                        score[counter] = (long) scoreEntry.getValue();
                                        ++counter;
                                    }
                                }
                            }
                            OnlineMatch match = new OnlineMatch(id, profile.getUserName(), opponent, userId, profile);
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
                tvTotalPlayed.setText(getString(R.string.totalPlay) + totalPlayed);
                tvTotalWin.setText(getString(R.string.totalWin) + totalWin);
                tvTotalLose.setText(getString(R.string.totalLose) + totalLose);

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
                HashMap<String, String> profilesMap = (HashMap<String, String>) dataSnapshot.getValue();
                Iterator i = profilesMap.entrySet().iterator();
                while (i.hasNext()) {
                    String str = "";
                    long[] scores = new long[3];
                    Map.Entry profileEntry = (Map.Entry) i.next();
                    HashMap<String, String> profileDataMap = (HashMap<String, String>) profileEntry.getValue();
                    Iterator i2 = profileDataMap.entrySet().iterator();
                    while (i2.hasNext()) {
                        Map.Entry profileDataEntry = (Map.Entry) i2.next();
                        if (profileDataEntry.getKey().equals("UserName") && opponent.equals(profileDataEntry.getValue()))
                            str = (String) profileDataEntry.getValue();
                        if (profileDataEntry.getKey().equals("OnlineMatches") && str.equals(opponent)) {
                            HashMap<String, String> onlineMatchInformation = (HashMap<String, String>) profileDataEntry.getValue();
                            Iterator i3 = onlineMatchInformation.entrySet().iterator();
                            while (i3.hasNext()) {
                                Map.Entry onlineMatchInformationEntry = (Map.Entry) i3.next();
                                if (onlineMatchInformationEntry.getKey().equals(id)) {
                                    HashMap<String, String> matchMap = (HashMap<String, String>) onlineMatchInformationEntry.getValue();
                                    Iterator i4 = matchMap.entrySet().iterator();
                                    while (i4.hasNext()) {
                                        Map.Entry matchEntry = (Map.Entry) i4.next();
                                        if (matchEntry.getKey().equals("Scores")) {
                                            HashMap<String, String> scoresMap = (HashMap<String, String>) matchEntry.getValue();
                                            TreeMap<String, String> sortedScoresMap = new TreeMap<>(scoresMap);
                                            Iterator i5 = sortedScoresMap.entrySet().iterator();
                                            int counter = 0;
                                            while (i5.hasNext()) {
                                                Map.Entry scoreEntry = (Map.Entry) i5.next();
                                                scores[counter] = (long) scoreEntry.getValue();
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
