package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.R;

public class MatchItemAdapter extends BaseAdapter {

    private Profile profile;
    private Button btnPlayMatch;
    private TextView tvPlayer1;
    private TextView tvPlayer2;
    private Context context;

    public MatchItemAdapter (Context context, Profile profile) {
        this.profile = profile;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        btnPlayMatch = convertView.findViewById(R.id.btnPlayMatch);
        tvPlayer1 = convertView.findViewById(R.id.tvPlayer1);
        tvPlayer2 = convertView.findViewById(R.id.tvPlayer2);

        tvPlayer1.setText(profile.getUserName());

        setPlayMatchListener();
        return convertView;
    }

    public void setPlayMatchListener() {
        btnPlayMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
    }

    private void startGame() {
        Intent intentGame = new Intent(context, GameActivity.class);
        intentGame.putExtra("profile", profile);
        context.startActivity(intentGame);
    }
}
