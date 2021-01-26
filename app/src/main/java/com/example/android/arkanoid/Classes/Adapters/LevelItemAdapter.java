package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.Activities.GameActivity;
import com.example.android.arkanoid.Classes.Level;
import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.SerializableBrick;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class LevelItemAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<Level> levels;
    private final ArrayList<ArrayList<SerializableBrick>> serializableBrickLists;
    private final Context context;
    private final Profile profile;

    public LevelItemAdapter(Context context, ArrayList<Level> levels, ArrayList<ArrayList<SerializableBrick>> serializableBrickLists, Profile profile) {
        inflater = (LayoutInflater.from(context));
        this.levels = levels;
        this.context = context;
        this.serializableBrickLists = serializableBrickLists;
        this.profile = profile;
    }

    @Override
    public int getCount() {
        return levels.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_level, null);

        final Level level = levels.get(position);
        TextView tvCreator = convertView.findViewById(R.id.tvCreator);
        TextView tvLevelName = convertView.findViewById(R.id.tvLevelName);
        Button bntPlayLevel = convertView.findViewById(R.id.btnPlayLevel);

        tvCreator.setText(level.getCreator());
        tvLevelName.setText(level.getLevelName());

        bntPlayLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGame = new Intent(context, GameActivity.class);
                intentGame.putExtra("level", serializableBrickLists.get(position));
                intentGame.putExtra("levelName", level.getLevelName());
                intentGame.putExtra("profile", profile);
                intentGame.putExtra("levelCreator", level.getCreator());
                context.startActivity(intentGame);
            }
        });
        return convertView;
    }
}
