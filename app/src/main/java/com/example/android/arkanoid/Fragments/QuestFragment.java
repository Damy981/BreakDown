package com.example.android.arkanoid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.example.android.arkanoid.Classes.DateChangeReceiver;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.Classes.Adapters.QuestItemAdapter;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;

public class QuestFragment extends Fragment {

    private ListView lvQuestItem;
    private ImageView btnBackQuest;
    private FileInputStream questFile;
    private Services services;
    private ArrayList<Quest> questsList = new ArrayList();
    private Profile profile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quest, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        Objects.requireNonNull(getContext()).registerReceiver(new DateChangeReceiver(), intentFilter);

        profile = (Profile) getArguments().getSerializable("profile");
        services = new Services(getActivity().getSharedPreferences(Services.SHARED_PREF_DIR, Context.MODE_PRIVATE), profile.getUserId());
        btnBackQuest = getView().findViewById(R.id.ivBackQuest);

        btnBackQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        getQuestListFromFile();

        lvQuestItem = getActivity().findViewById(R.id.lvQuestItem);

        QuestItemAdapter adapter = new QuestItemAdapter(getContext(), questsList);
        lvQuestItem.setAdapter(adapter);
    }

    //retrieve quests information from file
    private void getQuestListFromFile() {
        try {
            questFile = new FileInputStream(getActivity().getApplicationContext().getFilesDir() + "/" + services.getQuestsFileName());
            ObjectInputStream q = new ObjectInputStream(questFile);
            for(int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++){
                questsList.add((Quest) q.readObject());
            }
            q.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void resetDailyQuest() {
        getQuestListFromFile();
        for (int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++) {
            if (questsList.get(i).isDaily())
                questsList.get(i).setProgress(0);
        }
        services.updateQuestsFile(getContext(), questsList);
    }
}