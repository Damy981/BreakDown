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

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.R;

/*Fragment that manages the settings and udpate them in profile
  and local data.
*/

public class SettingsFragment extends Fragment {

    private Profile profile;
    private ToggleButton tbAccelerometer;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    private boolean tbAccelStatus;
    private ImageView ivBackSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /* on view created get settings from local data and add listeners
       for set on/off in profile and update local data when button is clicked */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile = (Profile) getArguments().getSerializable("profile");
        tbAccelerometer = getView().findViewById(R.id.swAccelerometer);
        ivBackSettings = getActivity().findViewById(R.id.ivBackSettings);
        getSettingsPreferences();
        tbAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tbAccelerometer.isChecked()) {

                    profile.setAccelerometer(true);
                }
                else {
                    profile.setAccelerometer(false);
                }
                setSettingsPreferences();
            }
        });

        ivBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    private void getSettingsPreferences() {
        preferences = getActivity().getSharedPreferences(Services.SHARED_PREF_DIR,Context.MODE_PRIVATE);
        tbAccelStatus = preferences.getBoolean("tbAccelStatus", false);
        tbAccelerometer.setChecked(tbAccelStatus);
    }

    private void setSettingsPreferences() {
        editor = preferences.edit();
        editor.putBoolean("tbAccelStatus", tbAccelerometer.isChecked()); // value to store
        editor.commit();
    }
}