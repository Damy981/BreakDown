package com.example.android.arkanoid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.arkanoid.Activities.LoginActivity;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.ProfileImageGenerator;
import com.example.android.arkanoid.Classes.Services;
import com.example.android.arkanoid.Classes.ShopItemAdapter;
import com.example.android.arkanoid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class ProfileFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private Button guestRegisterButton;
    private FirebaseUser user;
    private EditText etUsername;
    private TextView tvCoins;
    private FirebaseAuth mAuth;
    private ImageView profileImage;
    private ListView lvOwnedItems;
    private ImageView ivEditName;
    private Profile profile;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        guestRegisterButton = getView().findViewById(R.id.btnGuestRegister);
        guestRegisterButton.setOnClickListener(this);

        etUsername = getView().findViewById(R.id.etUsername);
        etUsername.setKeyListener(null);

        tvCoins = getView().findViewById(R.id.tvCoins);
        lvOwnedItems = getView().findViewById(R.id.lvOwnedItems);

        ivEditName = getView().findViewById(R.id.ivPencilProfile);
        ivEditName.setOnClickListener(this);

        profile = (Profile) getArguments().getSerializable("profile");
        profileImage = view.findViewById(R.id.imageView_profile);
        etUsername.setText(profile.getUserName());
        tvCoins.setText(String.valueOf(profile.getCoins()));

        ShopItemAdapter adapter;
        adapter = new ShopItemAdapter(this.getActivity(), profile, tvCoins, lvOwnedItems, false);
        lvOwnedItems.setAdapter(adapter);

        generateProfileImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onClick(View view) {
        if (view == guestRegisterButton) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        if (view == ivEditName) {
            etUsername.setKeyListener(new EditText(getContext()).getKeyListener());
            etUsername.addTextChangedListener(this);
        }
    }

    // Profile image generation
    private void generateProfileImage() {
        if (user != null) {
            new ProfileImageGenerator(getContext())
                    .fetchImageOf(profile.getUserName(), new ProfileImageGenerator.OnImageGeneratedListener() {
                        @Override
                        public void onImageGenerated(Drawable drawable) {
                            profileImage.setImageDrawable(drawable);
                        }
                    });
        } else {
            guestRegisterButton.setVisibility(View.VISIBLE);
            Drawable userImage = getResources().getDrawable(R.drawable.user_image);
            profileImage.setImageDrawable(userImage);
            ivEditName.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Services services = new Services(getContext().getSharedPreferences(Services.SHARED_PREF_DIR, Context.MODE_PRIVATE));
        services.setNameAndUserId(etUsername.getText().toString(), user.getUid());
    }
}