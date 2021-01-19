package com.example.android.arkanoid.Classes.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.arkanoid.Classes.Profile;
import com.example.android.arkanoid.Classes.Quest;
import com.example.android.arkanoid.R;

import java.util.ArrayList;

public class QuestItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private TextView questText, done, total, reward;
    private Button btnGetReward;
    private ArrayList<Quest> questsList;
    private Profile profile;


    public QuestItemAdapter(Context context, ArrayList<Quest> questsList, Profile profile) {
        this.questsList = questsList;
        inflater = (LayoutInflater.from(context));
        this.profile = profile;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_questitem, null);
        questText = convertView.findViewById(R.id.tvQuestText);
        done = convertView.findViewById(R.id.tvDone);
        total = convertView.findViewById(R.id.tvTotal);
        reward = convertView.findViewById(R.id.tvReward);
        btnGetReward = convertView.findViewById(R.id.btnGetReward);

        btnGetReward.setVisibility(View.GONE);
        questText.setText(questsList.get(position).getQuestText());
        done.setText(String.valueOf(questsList.get(position).getProgress()));
        total.setText(String.valueOf(questsList.get(position).getTarget()));
        reward.setText(String.valueOf(questsList.get(position).getReward()));

        if(profile.getQuestsList().get(position).isCompleted()) {
            btnGetReward.setVisibility(View.VISIBLE);
            if(!profile.getQuestsList().get(position).isRewardRedeemed()){
                btnGetReward.setText("Redeem");
                final View finalConvertView = convertView;
                btnGetReward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile.giveQuestReward(position);
                        Button btnGetReward = finalConvertView.findViewById(R.id.btnGetReward);
                        btnGetReward.setText("Redeemed");
                        btnGetReward.setEnabled(false);
                    }
                });
            }
            else{
                btnGetReward.setText("Redeemed");
                btnGetReward.setEnabled(false);
            }
        }

        return convertView;
    }
}
