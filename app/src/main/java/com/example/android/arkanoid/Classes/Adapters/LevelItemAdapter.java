package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.android.arkanoid.R;

public class LevelItemAdapter extends BaseAdapter {
    private final LayoutInflater inflater;

    public LevelItemAdapter(Context context) {
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
        convertView = inflater.inflate(R.layout.item_level, null);

        return convertView;
    }
}
