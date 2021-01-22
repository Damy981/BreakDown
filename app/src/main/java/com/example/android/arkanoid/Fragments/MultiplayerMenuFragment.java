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
import java.util.concurrent.ThreadLocalRandom;

public class MultiplayerMenuFragment extends Fragment {

    private Profile profile;
    private Button btnSearchOpponent;
    private ListView lvMatchItem;
    private FirebaseDatabase database;
    private String userId;
    private String userIdOpponent;
    private String usernameOpponent;
    private OnlineMatch match;
    private ArrayList<OnlineMatch> matchList;

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
        matchList = new ArrayList<>();

        ProgressBar progressBar = getView().findViewById(R.id.progressBarMulti);
        progressBar.setVisibility(View.VISIBLE);
        Button btnSearchOpponent  = getView().findViewById(R.id.btnSearchOpponent);
        btnSearchOpponent.setVisibility(View.GONE);

        setSearchOpponentListener();
        getAllMatch();
        setAdapter();
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
                DatabaseReference myRef2 = database.getReference("Profiles").child(userIdOpponent).child("OnlineMatches").child(id);
                myRef2.child("Opponent").setValue(profile.getUserName());
                myRef2.child("Status").setValue(OnlineMatch.IN_PROGRESS);
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
                long score[] = new long[3];
                String status = "";

                HashMap<String,String> matchesMap= (HashMap<String,String>) dataSnapshot.child("OnlineMatches").getValue();
                if(matchesMap != null) {
                    Iterator i = matchesMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (Map.Entry) i.next();
                        id = (String) entry.getKey();

                        HashMap<String, String> hm = (HashMap<String, String>) entry.getValue();
                        Iterator i2 = hm.entrySet().iterator();
                        while (i2.hasNext()) {
                            Map.Entry entry2 = (Map.Entry) i2.next();
                            if (entry2.getKey().equals("Opponent")) {
                                opponent = (String) entry2.getValue();
                            } 
                            else if (entry2.getKey().equals("Status")) {
                                status = (String) entry2.getValue();
                            }
                            else if (entry2.getKey().equals("Scores")) {
                                HashMap<String, String> hm2 = (HashMap<String, String>) entry2.getValue();
                                Iterator i3 = hm2.entrySet().iterator();
                                while (i3.hasNext()) {
                                    int counter = 0;
                                    Map.Entry entry3 = (Map.Entry) i3.next();
                                    score[counter] = (long) entry3.getValue();
                                }
                            }
                        }
                        OnlineMatch match = new OnlineMatch(id, profile.getUserName(), opponent, userId);
                        match.setStatus(status);
                        for (int j = 0; j < score.length; j++) {
                            match.setScore(score[j], j);
                        }
                        matchList.add(match);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setAdapter() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
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
}