package com.example.android.arkanoid.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class DateChangeReceiver extends BroadcastReceiver {

    private Services services;

    public DateChangeReceiver() {
    }

    public DateChangeReceiver(Services services) {
        this.services = services;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        resetDailyQuest(services, context);
    }

    private void resetDailyQuest(Services services, Context context) {
        ArrayList<Quest> questsList = services.getQuestListFromFile(context);
        for (int i = 0; i < Quest.QUEST_TOTAL_NUMBER; i++) {
            if (questsList.get(i).isDaily()) {
                Log.i("cacca", "Daily");
                questsList.get(i).resetProgress();
                questsList.get(i).setCompleted(false);
                questsList.get(i).setRewardRedeemed(false);
            }
        }
        services.updateQuestsFile(context, questsList);
    }
}