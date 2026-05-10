package com.example.p2project.IdleGame;
import android.os.Build;
import android.util.Log;

import com.example.p2project.IdleGame.DayNight.DayNightSystem;

import java.time.*;
public class CurrencyTracker
{
    public static Double treats = 0D;
    public static EarnThread earnThread;
    public static Instant lastOnline;
    public static Double curEarn()
    {
        if (!DayNightSystem.day) return 0D;
        Double curEarn = 0D;
        for (Animal curAnimal : Inventory.activeAnimals)
        {
            if (curAnimal != null) curEarn += curAnimal.production;
        }
        for (Animal curAnimal : Inventory.inventory)
        {
            if (curAnimal != null) curEarn *= curAnimal.passive/100D + 1D;
        }
        return curEarn;
    }
    public static Double[] offlineEarnings(Instant time) {
        Duration offlineTime;
        Double[] toReturn = {0D,0D,0D};
        if (time != null) {
            offlineTime = Duration.between(time, Instant.now());
            toReturn[0] = (double) offlineTime.getSeconds();
            toReturn[1] = activeBetween(time, 9, 21, 0);
            toReturn[2] = toReturn[1] * curEarn();
        }
        return toReturn;
    }
    public static Double activeBetween(Instant lastOnline, int startHour, int endHour, int endMinute)
    {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime start = lastOnline.atZone(zone);
        ZonedDateTime end = Instant.now().atZone(zone);
        double timeActive = 0D;

        ZonedDateTime currentDay = start.toLocalDate().atStartOfDay(zone);

        while (!currentDay.isAfter(end)) {
            ZonedDateTime windowStart = currentDay.withHour(startHour).withMinute(0).withSecond(0);
            ZonedDateTime windowEnd = currentDay.withHour(endHour).withMinute(endMinute).withSecond(0);

            ZonedDateTime overlapStart = start.isAfter(windowStart) ? start : windowStart;
            ZonedDateTime overlapEnd = end.isBefore(windowEnd) ? end : windowEnd;

            if (overlapStart.isBefore(overlapEnd)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    timeActive += Duration.between(overlapStart, overlapEnd).toSeconds();
                }
            }

            currentDay = currentDay.plusDays(1);
        }

        return timeActive;
    }
}

