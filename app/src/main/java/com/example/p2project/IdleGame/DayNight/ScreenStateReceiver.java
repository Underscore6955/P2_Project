package com.example.p2project.IdleGame.DayNight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.p2project.IdleGame.CurrencyTracker;

import java.time.Instant;

public class ScreenStateReceiver extends BroadcastReceiver {
    Instant screenOffTime;
    public static double screenOffTimeTotal = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            screenOffTimeTotal += CurrencyTracker.activeBetween(screenOffTime,21,23,59) +
                    CurrencyTracker.activeBetween(screenOffTime,0,9,0);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            screenOffTime = Instant.now();
        }
    }
}
