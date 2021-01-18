package com.example.android.arkanoid.Classes.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android.arkanoid.Classes.PowerUp;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class ProfileItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PowerUp> powerUps;
    private int[] images = {R.drawable.coin_drop, R.drawable.coins_image, R.drawable.freeze, R.drawable.explosion};


    public ProfileItemAdapter(Context context, ArrayList<PowerUp> powerUps ) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
        this.powerUps = powerUps;
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
        shopItemQuantity.setText("Quantity:" + String.valueOf(powerUps.get(i).getQuantity()));

        return view;
    }

}
