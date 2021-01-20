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
import android.widget.ListView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.Adapters.MatchItemAdapter;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class MultiplayerMenuFragment extends Fragment {

    private Profile profile;
    private Button btnSearchOpponent;
    private ListView lvMatchItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiplayer_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile = (Profile) getArguments().getSerializable("profile");
        lvMatchItem = getView().findViewById(R.id.lvMatch);
        btnSearchOpponent = getView().findViewById(R.id.btnSearchOpponent);

        setSearchOpponentListener();

        MatchItemAdapter adapter;
        adapter = new MatchItemAdapter(getContext(), profile);
        lvMatchItem.setAdapter(adapter);
    }

    private void setSearchOpponentListener() {
        btnSearchOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    private void startGame() {
        Intent intentGame = new Intent(getContext(), GameActivity.class);
        intentGame.putExtra("profile", profile);
        startActivity(intentGame);
    }
}