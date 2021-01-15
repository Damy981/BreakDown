package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android.arkanoid.Activities.MainActivity;
import com.example.android.arkanoid.Classes.PowerUp;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class ShopItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private Profile profile;
    private TextView tvShopCoins;
    private ListView lv;
    private ArrayList<PowerUp> powerUps;
    private boolean buyButtonEnabled;
    private int[] images = {R.drawable.coins_image, R.drawable.coins_image, R.drawable.freeze, R.drawable.explosion, R.drawable.coins_image};


    public ShopItemAdapter(Context context, Profile profile, TextView tvShopCoins, ListView lv, boolean buyButtonEnabled) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
        this.profile = profile;
        powerUps = profile.getPowerUps();
        this.tvShopCoins = tvShopCoins;
        this.lv = lv;
        this.buyButtonEnabled = buyButtonEnabled;
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
        view = inflater.inflate(R.layout.item_shopitem, null);
        TextView shopItemName = view.findViewById(R.id.tvItemName);
        final TextView shopItemQuantity = view.findViewById(R.id.tvQuantity);
        final ImageView buyItem = view.findViewById(R.id.btnBuy);
        final TextView tvBuyShop = view.findViewById(R.id.textView_buy_shop);
        CardView cvShopItem = view.findViewById(R.id.cvShop);
        ImageView image = view.findViewById(R.id.ivShopImagePowerUp);
        image.setImageResource(images[i]);

        cvShopItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout clBuyShopItem = view.findViewById(R.id.ClBuyShopItem);
                clBuyShopItem.setVisibility(VISIBLE);
            }
        });

        if(!buyButtonEnabled){
            buyItem.setVisibility(View.GONE);   //the list view is used in profile too, but we don't need buy button there
        }

        shopItemName.setText(powerUps.get(i).getName());
        tvBuyShop.setText("Buy for " + powerUps.get(i).getPrice() + "!");

        setQuantityText(i, shopItemQuantity);

        if (profile.getCoins() < powerUps.get(i).getPrice()){
            buyItem.setEnabled(false);
        }


        final int e = i;
        buyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            /*when button buy is clicked, call the buyItem function; then check if the user can afford
            to buy other items and if he/she can't, the buy button is disabled */
            public void onClick(View view) {
                buyItem(e);
                setQuantityText(e, shopItemQuantity);
                for(int i = 0; i < powerUps.size(); i++){
                    Button buyItem = lv.getChildAt(i).findViewById(R.id.btnBuy);
                    if (Integer.parseInt(String.valueOf(tvShopCoins.getText())) < powerUps.get(i).getPrice()) {
                        buyItem.setEnabled(false);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            /*when buy button is disabled the entire row is clickable; when the user clicks, a message is displayed
                            * this is called when buy button is set disabled after a purchase*/
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                noCoinsMessage();
                            }
                        });
                    }
                }
                tvBuyShop.setText("Buy for " + powerUps.get(e).getPrice() + "!");
            }
        });
        if (!buyItem.isEnabled()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                /*when buy button is disabled the entire row is clickable; when the user clicks, a message is displayed.
                * this is called when buy button is already disabled when the user open the shop*/
                public void onClick(View view) {
                    noCoinsMessage();
                }
            });
        }
        return view;
    }

    /*modify the amount of coins and power-ups owned by the user, and increment the
     price of the power up just bought if it is a stat improvement*/
    private void buyItem(int e) {
        profile.setCoins(profile.getCoins() - powerUps.get(e).getPrice());
        profile.setQuantities(powerUps.get(e).getQuantity() + 1, e);
        tvShopCoins.setText(String.valueOf(profile.getCoins()));
        if (e < Profile.STATS_NUMBER){
            profile.setPrices(powerUps.get(e).getPrice() + 5, e);
        }
        profile.updateProfile();
        Log.i("quantità", profile.getQuantities());
    }

    //contain the message displayed when the user does not have enough coins to buy a power up
    private void noCoinsMessage() {
        Toast.makeText(context, "Not enough coins", Toast.LENGTH_LONG).show();
    }

    private void setQuantityText(int i, TextView shopItemQuantity) {
        if(i == PowerUp.COINS_DROP_RATE) {
            shopItemQuantity.setText(powerUps.get(i).getQuantity() + "%");
        }else {
            shopItemQuantity.setText("Owned: " + powerUps.get(i).getQuantity());
        }
    }
}
