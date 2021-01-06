package com.example.android.arkanoid.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.arkanoid.Classes.PowerUp;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.ShopItemAdapter;
import com.example.android.arkanoid.R;

import java.util.ArrayList;
import java.util.List;


public class ShopFragment extends Fragment {

    private TextView tvShopCoins;
    private ListView lv;
    private int coins;
    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile = (Profile) getArguments().getSerializable("profile");
        coins = profile.getCoins();
        tvShopCoins = getView().findViewById(R.id.tvShopCoins);
        tvShopCoins.setText(String.valueOf(coins));

        lv = getActivity().findViewById(R.id.lvShopItem);
        ShopItemAdapter adapter;
        adapter = new ShopItemAdapter(this.getActivity(), profile, tvShopCoins, lv);
        lv.setAdapter(adapter);
    }

}