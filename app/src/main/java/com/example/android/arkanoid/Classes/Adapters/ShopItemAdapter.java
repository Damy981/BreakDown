package com.example.android.arkanoid.Classes.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android.arkanoid.Classes.PowerUp;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class ShopItemAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final Profile profile;
    private final TextView tvShopCoins;
    private final ArrayList<PowerUp> powerUps;
    private final int[] images = {R.drawable.coin_drop, R.drawable.paddle_length, R.drawable.freeze_two, R.drawable.explosion_two};

    public ShopItemAdapter(Context context, Profile profile, TextView tvShopCoins) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
        this.profile = profile;
        powerUps = profile.getPowerUps();
        this.tvShopCoins = tvShopCoins;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.item_shopitem, null);
        TextView shopItemName = convertView.findViewById(R.id.tvItemName);
        final TextView shopItemQuantity = convertView.findViewById(R.id.tvQuantity);
        ImageView ivBuyItem = convertView.findViewById(R.id.btnBuy);
        final TextView tvBuyShop = convertView.findViewById(R.id.textView_buy_shop);
        CardView cvShopItem = convertView.findViewById(R.id.cvShop);
        ImageView image = convertView.findViewById(R.id.ivShopImagePowerUp);
        image.setImageResource(images[i]);

        final int e = i;
        final View finalConvertView = convertView;
        cvShopItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout clBuyShopItem = view.findViewById(R.id.ClBuyShopItem);
                if(clBuyShopItem.getVisibility() == View.GONE){
                    clBuyShopItem.setVisibility(VISIBLE);
                    checkMaxStats(e, finalConvertView);
                }else{
                    clBuyShopItem.setVisibility(View.GONE);
                }
            }
        });

        shopItemName.setText(powerUps.get(i).getName());
        tvBuyShop.setText(context.getString(R.string.buyFor) + powerUps.get(i).getPrice() + "!");
        setQuantityText(i, shopItemQuantity);


        ivBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            /*when button buy is clicked, call the buyItem function; then check if the user can afford
            to buy other items and if he/she can't, the buy button is disabled */
            public void onClick(View view) {
                if (Integer.parseInt(String.valueOf(tvShopCoins.getText())) >= powerUps.get(e).getPrice()) {
                    buyItem(e);
                    checkMaxStats(e, finalConvertView);
                    setQuantityText(e, shopItemQuantity);
                    tvBuyShop.setText(context.getString(R.string.buyFor) + powerUps.get(e).getPrice() + "!");
                }else{
                    noCoinsMessage();
                }
            }
        });
        return convertView;
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
    }

    //contain the message displayed when the user does not have enough coins to buy a power up
    private void noCoinsMessage() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(R.string.notEnoughCoins)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setQuantityText(int i, TextView shopItemQuantity) {
        if(i == PowerUp.COINS_DROP_RATE) {
            shopItemQuantity.setText(powerUps.get(i).getQuantity() + "%");
        }
        else if(i == PowerUp.PADDLE_LENGTH) {
            shopItemQuantity.setText("+" + powerUps.get(i).getQuantity());
        }
        else {
            shopItemQuantity.setText(context.getString(R.string.owned) + powerUps.get(i).getQuantity());
        }
    }
    private void checkMaxStats(int i, View view) {
        if (i == PowerUp.COINS_DROP_RATE && powerUps.get(i).getQuantity() >= 80) {
            LinearLayout imgAndTextShop = view.findViewById(R.id.Img_and_text_shop);
            imgAndTextShop.setVisibility(View.INVISIBLE);
            TextView tvMax = view.findViewById(R.id.tvMax);
            tvMax.setVisibility(VISIBLE);
        }
        if (i == PowerUp.PADDLE_LENGTH && powerUps.get(i).getQuantity() >= 50) {
            LinearLayout imgAndTextShop = view.findViewById(R.id.Img_and_text_shop);
            imgAndTextShop.setVisibility(View.INVISIBLE);
            TextView tvMax = view.findViewById(R.id.tvMax);
            tvMax.setVisibility(VISIBLE);
        }
    }
}
