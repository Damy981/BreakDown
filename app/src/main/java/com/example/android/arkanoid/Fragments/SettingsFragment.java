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
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;

/*Fragment that manages the settings and udpate them in profile
  and local data.
*/

public class SettingsFragment extends Fragment {

    private ToggleButton tbAccelerometer;
    private ToggleButton tbMusic;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /* set buttons with settings got from local data and add listeners
       for set on/off in profile and update local data when button is clicked */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tbAccelerometer = getView().findViewById(R.id.tbAccelerometer);
        tbMusic = getView().findViewById(R.id.tbMusic);
        ImageView ivBackSettings = getActivity().findViewById(R.id.ivBackSettings);

        setButtons();
        setTbAccelerometerListener();
        setTbMusicListener();

        ivBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setButtons() {
        preferences = getActivity().getSharedPreferences(Services.SHARED_PREF_DIR,Context.MODE_PRIVATE);
        tbAccelerometer.setChecked(preferences.getBoolean("tbAccelStatus", false));
        tbMusic.setChecked(preferences.getBoolean("tbMusicStatus", true));
    }

    //set accelerometer on/off and save
    private void setTbAccelerometerListener() {
        tbAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tbAccelerometer.isChecked()) {

                    editor = preferences.edit();
                    editor.putBoolean("tbAccelStatus", true); // value to store
                    editor.commit();
                }
                else {
                    editor = preferences.edit();
                    editor.putBoolean("tbAccelStatus", false); // value to store
                    editor.commit();
                }
            }
        });
    }

    //set music on/off and save
    private void setTbMusicListener() {
        tbMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tbMusic.isChecked()) {
                    editor = preferences.edit();
                    editor.putBoolean("tbMusicStatus", true); // value to store
                    editor.commit();
                }
                else {
                    editor = preferences.edit();
                    editor.putBoolean("tbMusicStatus", false); // value to store
                    editor.commit();
                }
            }
        });
    }
}