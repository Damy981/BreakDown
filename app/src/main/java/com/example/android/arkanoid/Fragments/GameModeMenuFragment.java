package com.example.android.arkanoid.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Activities.MenuActivity;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class GameModeMenuFragment extends Fragment {

    private Button btnSinglePlayer;
    private Button btnMultiPlayer;
    private ImageView btnBack;
    private Profile profile;

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
        btnBack = getView().findViewById(R.id.btnBackModeMenu);

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
                if (isNetworkAvailable()) {
                    MenuActivity.fragment = new MultiplayerMenuFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("profile", profile);
                    MenuActivity.fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_place, MenuActivity.fragment);
                    fragmentTransaction.commit();
                }
                else
                    new AlertDialog.Builder(getContext())
                            .setTitle("No internet connection")
                            .setMessage("Internet connection required to play multiplayer")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}