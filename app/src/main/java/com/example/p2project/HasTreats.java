package com.example.p2project;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.Encoding;
import com.example.p2project.IdleGame.Inventory;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;

public class HasTreats extends AppCompatActivity
{
    public TextView treatView;
    public TextView earnView;
    public DayNightSystem dayNightSystem;
    protected View view;
    protected Encoding encoding = new Encoding(this);
    public void onPause()
    {
        super.onPause();
        if (dayNightSystem != null) dayNightSystem.running = false;
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        try {encoding.saveInventory(Inventory.inventory);} catch (IOException e) {}
        data.edit().putBoolean("hasBeenSaved", true).apply();
        data.edit().putString("lastOnline", Instant.now().toString()).apply();
    }
    public void onResume()
    {
        super.onResume();
        CurrencyTracker.earnThread.main = this;
        dayNightSystem = new DayNightSystem();
        setBackground();
        String[] idleRewards;
        if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("hasBeenSaved", false))
        {
            idleRewards = getIdleReward();
            if (Integer.parseInt(idleRewards[0]) > 30) {tellUserIdleRewards(idleRewards);}
        }

        dayNightSystem.start();
    }
    protected void insertButtons()
    {
        for (int i = 0; i <= 11; i++)
        {
            Inventory.inventorySlots[i] = new Inventory.InventorySlot(i, NewButton("inventory", i));
        }
        insertActive("inv_background");
    }
    protected  void insertActive(String defaultIcon)
    {
        for (int i = 0; i < 5; i++)
        {
            Inventory.activeSlots[i] = new Inventory.ActiveSlot(i, NewButton("active", i), defaultIcon);
        }
    }
    ImageButton NewButton(String type, Integer id)
    {
        return findViewById(getResources().getIdentifier((type + id), "id", getPackageName()));
    }
    String[] getIdleReward()
    {
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        Instant lastOnline;
        try { lastOnline = Instant.parse(data.getString("lastOnline", "null"));} catch (Exception e) {lastOnline = null;}
        Double[] offlineRewards = CurrencyTracker.offlineEarnings(lastOnline);
        if (offlineRewards[0] > 0D)
        {
            CurrencyTracker.treats += offlineRewards[2];
            CurrencyTracker.earnThread.updateText();
            Log.d("offline", "You were offline for " + offlineRewards[0] + " seconds, of this, your animals were active for " + offlineRewards[1] +" seconds and earned " + offlineRewards[2] + " treats");
        } else {Log.d("not offline",  "You were not offline for long enough");}
        String[] toReturn = new String[3];
        toReturn[0] = offlineRewards[0].intValue() + "";
        toReturn[1] = offlineRewards[2].intValue() + "";
        toReturn[2] = giveXp(lastOnline);
        return toReturn;
    }
    void tellUserIdleRewards(String[] message)
    {
        new Thread(() -> {
            Snackbar.make(view, ("You were offline for " + message[0] + " seconds and earned " + message[1] + " treats."), Snackbar.LENGTH_LONG).show();
            try {Thread.sleep(5000);} catch (InterruptedException e) {}
            Snackbar.make(view, ("Your animals earned " + message[2] + " xp."), Snackbar.LENGTH_LONG).show();
        }).start();
    }
    String giveXp(Instant time)
    {
        Double earnedXp = (CurrencyTracker.activeBetween(time, 21, 23,59) + CurrencyTracker.activeBetween(time, 0, 9,0)) / 40D;
        Log.d("xp", "Your animals earned " + earnedXp + " xp");
        for (Animal curAnimal : Inventory.activeAnimals)
        {
            if (curAnimal != null)
            {
                earnedXp += 100;
                curAnimal.giveXp(earnedXp, this);
            }
        }
        return earnedXp.intValue() + "";
    }
    protected void setBackground() {}
}
