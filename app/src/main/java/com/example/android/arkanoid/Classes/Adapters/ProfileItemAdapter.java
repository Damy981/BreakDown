package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.arkanoid.Classes.PowerUp;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class ProfileItemAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<PowerUp> powerUps;
    private final int[] images = {R.drawable.coin_drop, R.drawable.paddle_length, R.drawable.freeze_two, R.drawable.explosion_two};
    private Context context;

    public ProfileItemAdapter(Context context, ArrayList<PowerUp> powerUps ) {
        inflater = (LayoutInflater.from(context));
        this.powerUps = powerUps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return powerUps.size();
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
        view = inflater.inflate(R.layout.item_profileitem, null);
        TextView shopItemName = view.findViewById(R.id.tvItemName);
        TextView shopItemQuantity = view.findViewById(R.id.tvQuantity);
        ImageView image = view.findViewById(R.id.ivShopImagePowerUp);
        image.setImageResource(images[i]);
        shopItemName.setText(powerUps.get(i).getName());
        setQuantityText(i, shopItemQuantity);


        return view;
    }

    //set the quantity text in the textview
    private void setQuantityText(int i, TextView shopItemQuantity) {
        if(i == PowerUp.COINS_DROP_RATE) {
            shopItemQuantity.setText(powerUps.get(i).getQuantity() + "%");
        }
        else if(i == PowerUp.PADDLE_LENGTH) {
            shopItemQuantity.setText("+" + powerUps.get(i).getQuantity());
        }
        else {
            shopItemQuantity.setText(context.getString(R.string.quantity_2) + " " + String.valueOf(powerUps.get(i).getQuantity()));
        }
    }

}
