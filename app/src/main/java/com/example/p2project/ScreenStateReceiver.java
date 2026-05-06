package com.example.p2project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenStateReceiver extends BroadcastReceiver {
    double screenOffTime = 0;
    double screenOffTimeTotal = 0;
    double screenOnTimestamp = 0;
    double screenOnTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.d("SCREENCHECK", "SCREEN TURNED ON");
            screenOnTimestamp = System.currentTimeMillis();
            if( screenOffTimeTotal == 0) {
                screenOffTimeTotal = System.currentTimeMillis() - screenOffTime;
                Log.d("SCREENCHECK", "Screen was turned off for" + screenOffTimeTotal / 1000 + "seconds");
            } else if (screenOffTimeTotal != 0){
                screenOffTimeTotal = screenOffTimeTotal + (System.currentTimeMillis()-screenOffTime);
                Log.d("SCREENCHECK","Screen has been off for a tota of " + screenOffTimeTotal/1000 + "seconds");
            }
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.d("SCREENCHECK", "SCREEN TURNED OFF");
            screenOffTime = System.currentTimeMillis();
            screenOnTime = System.currentTimeMillis() - screenOnTimestamp;
            Log.d("SCREENCHECK", "Screen was on for " + screenOnTime/1000 + " Seconds");
        }
        if (screenOnTime >= 60000) {
            Log.d("SCREENCHECK", "Phone was on for over a minute. Resetting sleep time");

        }
        screenOffTimeTotal = 0;
    }
}
