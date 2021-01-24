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
import android.widget.TextView;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Adapters.ShopItemAdapter;
import com.example.android.arkanoid.R;


public class ShopFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Profile profile = (Profile) getArguments().getSerializable("profile");
        int coins = profile.getCoins();
        TextView tvShopCoins = getView().findViewById(R.id.tvShopCoins);
        tvShopCoins.setText(String.valueOf(coins));
        ImageView ivBackShop = getActivity().findViewById(R.id.ivBackShop);

        ListView lv = getActivity().findViewById(R.id.lvShopItem);

        ShopItemAdapter adapter;
        adapter = new ShopItemAdapter(this.getActivity(), profile, tvShopCoins);
        lv.setAdapter(adapter);

        ivBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}