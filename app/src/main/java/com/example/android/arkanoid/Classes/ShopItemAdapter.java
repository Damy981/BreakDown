package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.R;

public class ShopItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] items;
    private int[] prices;
    private Profile profile;
    private TextView tvShopCoins;

    public ShopItemAdapter(Context context, String[] items, int[] prices, Profile profile, TextView tvShopCoins) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
        this.items = items;
        this.prices = prices;
        this.profile = profile;
        this.tvShopCoins = tvShopCoins;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_shopitem, null);
        TextView shopItemName = view.findViewById(R.id.tvItemName);
        final Button buyItem = view.findViewById(R.id.btnBuy);
        shopItemName.setText(items[i]);
        buyItem.setText("Buy for " + prices[i]);
        if (profile.getCoins() < prices[i]){
            buyItem.setEnabled(false);
        }

        final int e = i;
        buyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem(e);
                if (profile.getCoins() < prices[e]) {
                    buyItem.setEnabled(false);
                }
                buyItem.setText("Buy for " + prices[e]);
                Log.i("shop", String.valueOf(profile.getCoins()) + "  " + prices[e]);
            }
        });
        return view;
    }

    private void buyItem(int e) {
        profile.setCoins(profile.getCoins() - prices[e]);
        tvShopCoins.setText(String.valueOf(profile.getCoins()));
        prices[e] = prices[e] + 5;
        profile.updateProfile();
    }
}
