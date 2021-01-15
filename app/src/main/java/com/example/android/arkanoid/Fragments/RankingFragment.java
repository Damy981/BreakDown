package com.example.android.arkanoid.Fragments;

import android.app.AlertDialog;
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
import com.example.android.arkanoid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RankingFragment extends Fragment {

    private ImageView btnBackRanking;
    private ListView lvRankItem;
    private HashMap<String,String> rankingMap;
    private ArrayList<String> rankingUsername;
    private ArrayList<String> rankingBestScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBackRanking = getActivity().findViewById(R.id.ivBackRanking);
        btnBackRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        lvRankItem = getActivity().findViewById(R.id.lvRankItem);

        rankingUsername = new ArrayList<String>();
        rankingBestScore = new ArrayList<String>();

        rankingMap = SplashScreenActivity.rankingMap;

        if(rankingMap != null) {
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
}