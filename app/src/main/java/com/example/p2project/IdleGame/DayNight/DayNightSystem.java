package com.example.p2project.IdleGame.DayNight;

import android.util.Log;
import android.widget.ImageView;
import com.example.p2project.R;
import java.time.*;

public class DayNightSystem{
    public ImageView background;
    public static boolean day = true;
    LocalTime time = LocalTime.now();
    LocalTime bedTime = LocalTime.of(21, 0);
    LocalTime awakeTime = LocalTime.of(9, 0);

    public void Time(){

        if(time.isBefore(bedTime) && time.isAfter(awakeTime)){
            day = true;
            background.setImageResource(R.drawable.day_background);
        }
        else {
            day = false;
            background.setImageResource(R.drawable.night_background);
        }
    }
}
