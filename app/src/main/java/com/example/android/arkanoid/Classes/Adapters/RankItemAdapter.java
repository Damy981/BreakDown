package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class RankItemAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final ArrayList<String> rankingUsername;
    private final ArrayList<String> rankingBestScore;

    public RankItemAdapter(Context context, ArrayList<String> rankingUsername, ArrayList<String> rankingBestScore) {
        this.rankingUsername = rankingUsername;
        this.rankingBestScore = rankingBestScore;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return rankingUsername.size();
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
        convertView = inflater.inflate(R.layout.item_rankitem, null);
        TextView tvRankUsername = convertView.findViewById(R.id.tvRankUsername);
        TextView tvRankBestScore = convertView.findViewById(R.id.tvRankBestScore);
        TextView tvNumberRanking = convertView.findViewById(R.id.tvNumberRanking);

        tvRankUsername.setText(rankingUsername.get(position));
        tvRankBestScore.setText(rankingBestScore.get(position));
        tvNumberRanking.setText("#" + toString().valueOf(position+1));

        return convertView;
    }

}
