package com.example.p2project.IdleGame;
import android.os.Build;

import java.time.*;
public class CurrencyTracker
{
    public static long treats = 0;
    public static EarnThread earnThread;
    public static Instant lastOnline;
    public static long curEarn()
    {
        long curEarn = 0;
        for (Inventory.ActiveSlot curSlot : Inventory.activeSlots)
        {
            if (curSlot.content != null) curEarn += curSlot.content.production;
        }
        for (Animal curAnimal : Inventory.inventory)
        {
            if (curAnimal != null) curEarn *= Math.divideExact(curAnimal.passive, 100L) + 1L;
        }
        return curEarn;
    }
    public static long offlineEarnings(Instant time) {
        Duration offlineTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && time != null) {
            offlineTime = Duration.between(time, Instant.now());
            return offlineTime.getSeconds() * curEarn();
        }
        return 0;
    }
}

