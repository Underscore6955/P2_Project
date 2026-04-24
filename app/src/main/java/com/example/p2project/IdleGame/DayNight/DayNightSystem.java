package com.example.p2project.IdleGame.DayNight;

import android.util.Log;

import java.text.DateFormat;
import java.time.*;
import java.util.Locale;

public class DayNightSystem{
    public boolean day = true;
    LocalTime time = LocalTime.now();
    LocalTime bedTime = LocalTime.of(21, 0);
    LocalTime awakeTime = LocalTime.of(9, 0);

    public void Time(){
        if(time.isBefore(bedTime) && time.isAfter(awakeTime)){
            day = true;
        }
        else {
            day = false;
        }
        Log.d("Day", "Day?: " + day);
    }
}
