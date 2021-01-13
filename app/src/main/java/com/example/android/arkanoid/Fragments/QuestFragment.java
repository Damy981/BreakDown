package com.example.android.arkanoid.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.Classes.QuestAdapter;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class QuestFragment extends Fragment {

    ListView lvQuestItem;
    ImageView btnBackQuest;
    FileInputStream questFile;
    Services services = new Services();
    private ArrayList questsList = new ArrayList();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quest, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBackQuest = getView().findViewById(R.id.ivBackQuest);

        btnBackQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //retrieve quests information from file
        try {
            questFile = getActivity().openFileInput(services.questsFileName);
            ObjectInput q = new ObjectInputStream(questFile);
            questsList = (ArrayList<Quest>) q.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        lvQuestItem = getActivity().findViewById(R.id.lvQuestItem);

        QuestAdapter adapter = new QuestAdapter(getContext(), questsList);
        lvQuestItem.setAdapter(adapter);

    }
}

