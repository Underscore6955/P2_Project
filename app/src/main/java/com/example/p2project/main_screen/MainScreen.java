package com.example.p2project.main_screen;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.HasTreats;
import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.DayNight.ScreenStateReceiver;
import com.example.p2project.IdleGame.EarnThread;
import com.example.p2project.IdleGame.Encoding;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.InventoryScreen;
import com.example.p2project.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.time.Instant;

public class MainScreen extends HasTreats
{
    static Boolean hasBeenStarted = false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainScreenActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        //data.edit().putBoolean("hasBeenSaved", false).apply();
        //data.edit().putBoolean("firstRun", true).apply();
        if (data.getBoolean("firstRun", true))
        {
            Inventory.inventory.add(new Animal("Polar_Bear",1000D, 1D));
            Inventory.inventory.add(new Animal("Cat",1000D, 1D));
            Inventory.inventory.add(new Animal("Chameleon",1000D, 1D));
            Inventory.inventory.add(new Animal("Chinchilla",1000D, 1D));
            Inventory.inventory.add(new Animal("Crab",1000D, 1D));
            Inventory.inventory.add(new Animal("Donkey",1000D, 1D));
            Inventory.inventory.add(new Animal("Fox",1000D, 1D));
            Inventory.inventory.add(new Animal("Jellyfish",1000D, 1D));
            Inventory.inventory.add(new Animal("Panda",1000D, 1D));
            data.edit().putString("endPeriod", Instant.now().plusSeconds(7*24*3600).toString()).apply();
            data.edit().putBoolean("firstRun", false).apply();
        }
        treatView = findViewById(R.id.treat_view_main);
        earnView = findViewById(R.id.earn_view_main);
        if (!hasBeenStarted)
        {
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            ScreenStateReceiver mReceiver = new ScreenStateReceiver();
            registerReceiver(mReceiver, intentFilter);
            loadIdle();
            startIdle();
            hasBeenStarted = true;
        }
        insertActive("empty");
        Inventory.updateActive("empty");
        ChangeScreenButton invButton = new ChangeScreenButton(findViewById(R.id.changeToInventory), InventoryScreen.class, this, dayNightSystem);
        invButton.button.setOnClickListener(v -> invButton.clicked());
    }
    @Override
    public void onResume()
    {
        super.onResume();
        for (Inventory.ActiveSlot curSlot : Inventory.activeSlots) {curSlot.startAnim();}
        dayNightSystem.background = findViewById(R.id.background_img_main);
        if (Instant.now().isAfter(Instant.parse(getSharedPreferences("data", MODE_PRIVATE).getString("endPeriod", "null")))) endPeriod();
    }
    void startIdle()
    {
        if (CurrencyTracker.earnThread != null) {CurrencyTracker.earnThread.stopEarn();}
        CurrencyTracker.earnThread = new EarnThread(this);
        CurrencyTracker.earnThread.start();
        if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("hasBeenSaved", false)) tellUserIdleRewards(getIdleReward());
    }
    void loadIdle()
    {
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        if (data.getBoolean("firstRun", true)) return;
        if (data.getBoolean("hasBeenSaved", false)) {try {encoding.loadInventory();} catch (Exception e) {}}
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
    String giveXp(Instant time)
    {
        Double earnedXp = (CurrencyTracker.activeBetween(time, 21, 23,59) + CurrencyTracker.activeBetween(time, 0, 9,0)) / 40D;
        Log.d("xp", "Your animals earned " + earnedXp + " xp");
        for (Animal curAnimal : Inventory.activeAnimals)
        {
            if (curAnimal != null)
            {
                curAnimal.giveXp(earnedXp);
            }
        }
        return earnedXp.intValue() + "";
    }
    void tellUserIdleRewards(String[] message)
    {
        new Thread(() -> {
            Snackbar.make(findViewById(R.id.mainScreenActivity), ("You were offline for " + message[0] + " seconds and earned " + message[1] + " treats."), Snackbar.LENGTH_LONG).show();
            try {Thread.sleep(5000);} catch (InterruptedException e) {}
            Snackbar.make(findViewById(R.id.mainScreenActivity), ("Your animals earned " + message[2] + " xp."), Snackbar.LENGTH_LONG).show();
        }).start();
    }
    void endPeriod()
    {
        Double averageSleep = ScreenStateReceiver.screenOffTimeTotal/3600/7;
        Log.d("totalsleep","During the week, you slept for " + ScreenStateReceiver.screenOffTimeTotal/3600 + " hours, thats " + averageSleep + " hours per night on average");

    }
}
