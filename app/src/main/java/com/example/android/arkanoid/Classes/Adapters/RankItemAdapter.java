package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.arkanoid.R;

public class RankItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private TextView tvRankUsername, tvRankBestScore;

    public RankItemAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
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
        convertView = inflater.inflate(R.layout.item_rankitem, null);
        tvRankUsername = convertView.findViewById(R.id.tvRankUsername);
        tvRankBestScore = convertView.findViewById(R.id.tvRankBestScore);
        return convertView;
    }
}
