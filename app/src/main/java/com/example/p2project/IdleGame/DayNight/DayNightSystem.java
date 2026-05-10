package com.example.p2project.IdleGame.DayNight;

import android.util.Log;
import android.widget.ImageView;
import com.example.p2project.R;
import java.time.*;

public class DayNightSystem extends Thread{
    public static boolean day = true;
    public Boolean running = true;
    public ImageView background;
    @Override
    public void run(){
        while (running) {
            LocalTime time = LocalTime.now();
            LocalTime bedTime = LocalTime.of(21, 0);
            LocalTime awakeTime = LocalTime.of(9, 0);
            if (time.isBefore(bedTime) && time.isAfter(awakeTime)) {
                day = true;
                Log.d("day", "it's day");
                if (background != null) background.setImageResource(R.drawable.day_background);
            } else {
                day = false;
                Log.d("night", "it's night");
                if (background != null) background.setImageResource(R.drawable.night_background);
            }
            try {Thread.sleep(30000);} catch (InterruptedException e) {}
        }
    }
}
