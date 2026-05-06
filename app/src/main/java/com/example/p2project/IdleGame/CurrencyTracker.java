package com.example.p2project.IdleGame;
import android.os.Build;

import java.time.*;
public class CurrencyTracker
{
    public static Double treats = 0D;
    public static EarnThread earnThread;
    public static Instant lastOnline;
    public static Double curEarn()
    {
        Double curEarn = 0D;
        for (Inventory.ActiveSlot curSlot : Inventory.activeSlots)
        {
            if (curSlot.content != null) curEarn += curSlot.content.production;
        }
        for (Animal curAnimal : Inventory.inventory)
        {
            if (curAnimal != null) curEarn *= curAnimal.passive/100D + 1D;
        }
        return curEarn;
    }
    public static Double offlineEarnings(Instant time) {
        Duration offlineTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && time != null) {
            offlineTime = Duration.between(time, Instant.now());
            return offlineTime.getSeconds() * curEarn();
        }
        return 0D;
    }
}

