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
    private final LayoutInflater inflater;
    private final ArrayList<Quest> questsList;
    private final Profile profile;


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
        TextView questText = convertView.findViewById(R.id.tvQuestText);
        TextView done = convertView.findViewById(R.id.tvDone);
        TextView total = convertView.findViewById(R.id.tvTotal);
        TextView reward = convertView.findViewById(R.id.tvReward);
        Button btnGetReward = convertView.findViewById(R.id.btnGetReward);

        btnGetReward.setVisibility(View.GONE);
        questText.setText(questsList.get(position).getQuestText());
        done.setText(String.valueOf(questsList.get(position).getProgress()));
        total.setText(String.valueOf(questsList.get(position).getTarget()));
        reward.setText(String.valueOf(questsList.get(position).getReward()));

        //if the user complete a quest, a redeem reward button is displayed
        if(profile.getQuestsList().get(position).isCompleted()) {
            btnGetReward.setVisibility(View.VISIBLE);
            if(!profile.getQuestsList().get(position).isRewardRedeemed()){
                btnGetReward.setText(R.string.redeem);
                final View finalConvertView = convertView;
                btnGetReward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //when the user redeem the reward, the button is disabled
                        profile.giveQuestReward(position);
                        Button btnGetReward = finalConvertView.findViewById(R.id.btnGetReward);
                        btnGetReward.setText(R.string.redeemed);
                        btnGetReward.setEnabled(false);
                    }
                });
            }
            else{
                btnGetReward.setText(R.string.redeemed);
                btnGetReward.setEnabled(false);
            }
        }

        return convertView;
    }
}
