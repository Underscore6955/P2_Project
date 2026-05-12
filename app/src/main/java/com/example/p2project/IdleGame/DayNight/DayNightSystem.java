package com.example.p2project.IdleGame.DayNight;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import com.example.p2project.R;
import java.time.*;

public class DayNightSystem extends Thread{
    public static boolean day = true;
    public Boolean running = true;
    public ImageView background;
    public int dayBackground;
    public int nightBackground;
    @Override
    public void run(){
        while (running) {
            LocalTime time = LocalTime.now();
            LocalTime bedTime = LocalTime.of(21, 0);
            LocalTime awakeTime = LocalTime.of(9, 0);
            if (time.isBefore(bedTime) && time.isAfter(awakeTime)) {
                day = true;
                Log.d("day", "it's day");
                if (background != null && dayBackground != 0) {
                    background.post(() -> background.setImageResource(dayBackground));
                }
            } else {
                day = false;
                Log.d("night", "it's night");
                if (background != null  && nightBackground != 0) {
                    background.post(() -> background.setImageResource(nightBackground));
                }
            }
            try {Thread.sleep(30000);} catch (InterruptedException e) {}
        }
    }
}
