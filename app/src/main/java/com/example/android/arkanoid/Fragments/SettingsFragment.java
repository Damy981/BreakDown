package com.example.android.arkanoid.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class SettingsFragment extends Fragment {

    private Profile profile;
    private ToggleButton tbAccelerometer;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    private boolean tbAccelStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile = (Profile) getArguments().getSerializable("profile");
        tbAccelerometer = getView().findViewById(R.id.swAccelerometer);
        getPreferences();
        tbAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tbAccelerometer.isChecked()) {

                    profile.setAccelerometer(true);
                }
                else {
                    profile.setAccelerometer(false);
                }
                setPreferences();
            }
        });
    }
    private void getPreferences() {
        preferences = getActivity().getSharedPreferences("com.example.android.arkanoid_preferences" ,Context.MODE_PRIVATE);
        tbAccelStatus = preferences.getBoolean("tbAccelStatus", false);
        tbAccelerometer.setChecked(tbAccelStatus);
    }

    private void setPreferences() {
        editor = preferences.edit();
        editor.putBoolean("tbAccelStatus", tbAccelerometer.isChecked()); // value to store
        editor.commit();
    }
}