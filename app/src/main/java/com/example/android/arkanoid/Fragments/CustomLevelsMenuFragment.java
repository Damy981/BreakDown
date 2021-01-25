package com.example.android.arkanoid.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.arkanoid.Classes.Adapters.LevelItemAdapter;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class CustomLevelsMenuFragment extends Fragment {

    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_levels_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = (Profile) getArguments().getSerializable("profile");
        ImageView btnBack = getView().findViewById(R.id.ivBackCustomLevels);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ListView lvCustomLevels = getView().findViewById(R.id.lvCustomLevels);
        LevelItemAdapter adapter = new LevelItemAdapter(getContext());
        lvCustomLevels.setAdapter(adapter);
    }
}