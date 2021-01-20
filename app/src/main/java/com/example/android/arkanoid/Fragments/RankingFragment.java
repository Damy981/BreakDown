package com.example.android.arkanoid.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.arkanoid.Activities.SplashScreenActivity;
import com.example.android.arkanoid.Classes.Adapters.RankItemAdapter;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RankingFragment extends Fragment {

    private ImageView btnBackRanking;
    private ListView lvRankItem;
    private HashMap<String,String> rankingMap;
    private ArrayList<String> rankingUsername;
    private ArrayList<String> rankingBestScore;
    private ImageView btnShare;
    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = (Profile) getArguments().getSerializable("profile");

        btnBackRanking = getActivity().findViewById(R.id.ivBackRanking);
        btnBackRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnShare = getActivity().findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLeaderBoard();
            }
        });

        lvRankItem = getActivity().findViewById(R.id.lvRankItem);

        rankingUsername = new ArrayList<>();
        rankingBestScore = new ArrayList<>();

        rankingMap = SplashScreenActivity.rankingMap;

        if(rankingMap != null) {
            rankingMap = sortHashMapByValues(rankingMap);

            Iterator i = rankingMap.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry pair = (Map.Entry) i.next();
                rankingUsername.add(pair.getKey().toString());
                rankingBestScore.add(pair.getValue().toString());
            }

            RankItemAdapter adapter = new RankItemAdapter(getContext(), rankingUsername, rankingBestScore);
            lvRankItem.setAdapter(adapter);
        }
        else
            new AlertDialog.Builder(getContext())
                    .setTitle("No internet connection")
                    .setMessage("Please connect to internet and restart your application to see the ranking")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
    }

    private LinkedHashMap<String, String> sortHashMapByValues(
            HashMap<String, String> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        Collections.reverse(mapValues);
        Collections.reverse(mapKeys);

        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();

        while (valueIt.hasNext()) {
            String val = String.valueOf(valueIt.next());
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String comp1 = String.valueOf(passedMap.get(key));
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    private void shareLeaderBoard() {
        Iterator iterator = rankingMap.entrySet().iterator();
        int i = 0;
        String score = "0";
        while (iterator.hasNext()) {
            i++;
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getKey().equals(profile.getUserName())) {
                score = entry.getValue().toString();
                break;
            }
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi! " + profile.getUserName() + " is in position " + String.valueOf(i) + " with a score of " + score + " on Arkanoid, come play with us!");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}