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
import android.widget.TextView;

import com.example.android.arkanoid.Activities.RegistrationActivity;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Button guestRegisterButton;
    private FirebaseUser user;
    private TextView tvUsername;
    private TextView tvCoins;
    private FirebaseAuth mAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        guestRegisterButton = getView().findViewById(R.id.btnGuestRegister);
        guestRegisterButton.setOnClickListener(this);

        tvUsername = getView().findViewById(R.id.tvUsername);
        tvCoins = getView().findViewById(R.id.tvCoins);

        Profile profile = (Profile) getArguments().getSerializable("profile");

        tvUsername.setText(profile.getUserName());
        tvCoins.setText(String.valueOf(profile.getCoins()));

        if (user.isAnonymous()) {
            guestRegisterButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
        startActivity(intent);
    }
}