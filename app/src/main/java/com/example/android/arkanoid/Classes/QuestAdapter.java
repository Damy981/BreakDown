package com.example.android.arkanoid.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class QuestAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    TextView questText, done, total, reward;
    private ArrayList<Quest> questsList = new ArrayList();


    public QuestAdapter(Context context, ArrayList<Quest> questsList) {
        this.questsList = questsList;
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return questsList.size();
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
        convertView = inflater.inflate(R.layout.item_questitem, null);
        questText = convertView.findViewById(R.id.tvQuestText);
        done = convertView.findViewById(R.id.tvDone);
        total = convertView.findViewById(R.id.tvTotal);
        reward = convertView.findViewById(R.id.tvReward);

        questText.setText(questsList.get(position).getQuestText());
        done.setText(String.valueOf(questsList.get(position).getProgress()));
        total.setText(String.valueOf(questsList.get(position).getTarget()));
        reward.setText(String.valueOf(questsList.get(position).getReward()));

        return convertView;
    }
}
