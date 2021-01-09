package com.example.android.arkanoid.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class GameModeMenuFragment extends Fragment {

    Button btnSinglePlayer;
    Button btnMultiPlayer;
    Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_mode_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = (Profile) getArguments().getSerializable("profile");

        btnSinglePlayer = getView().findViewById(R.id.btnSinglePlayer);
        btnMultiPlayer = getView().findViewById(R.id.btnMultiPlayer);

        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGame = new Intent(getContext(), GameActivity.class);
                intentGame.putExtra("profile", profile);
                startActivity(intentGame);
            }
        });

        btnMultiPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}