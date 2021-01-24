package com.example.android.arkanoid.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.Classes.Adapters.QuestItemAdapter;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class QuestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quest, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Profile profile = (Profile) getArguments().getSerializable("profile");
        Services services = new Services(getActivity().getSharedPreferences(Services.SHARED_PREF_DIR, Context.MODE_PRIVATE), profile.getUserId());
        ImageView btnBackQuest = getView().findViewById(R.id.ivBackQuest);

        btnBackQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ArrayList<Quest> questsList = services.getQuestListFromFile(getContext());

        ListView lvQuestItem = getActivity().findViewById(R.id.lvQuestItem);

        QuestItemAdapter adapter = new QuestItemAdapter(getContext(), questsList, profile);
        lvQuestItem.setAdapter(adapter);
    }
}