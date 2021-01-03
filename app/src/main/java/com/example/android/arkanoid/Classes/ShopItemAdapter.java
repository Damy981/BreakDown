package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.R;

public class ShopItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    String[] items;
    int[] prices;

    public ShopItemAdapter(Context context, String[] items, int[] prices) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
        this.items = items;
        this.prices = prices;
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
        Button buyItem = view.findViewById(R.id.btnBuy);
        shopItemName.setText(items[i]);
        buyItem.setText("Buy for " + prices[i]);
        //buyItem.setOnClickListener();
        return view;
    }
}
