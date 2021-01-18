package com.example.android.arkanoid.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.arkanoid.Activities.MenuActivity;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MenuActivity.setDayChanged(true);
    }
}