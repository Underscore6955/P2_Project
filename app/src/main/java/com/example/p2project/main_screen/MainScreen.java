package com.example.p2project.main_screen;

import android.app.Dialog;
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

import com.example.p2project.AudioPlayer;
import com.example.p2project.HasTreats;
import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.DayNight.ScreenStateReceiver;
import com.example.p2project.IdleGame.EarnThread;
import com.example.p2project.IdleGame.Encoding;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.IdleGame.ShopScreen;
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
        view = findViewById(R.id.mainScreenActivity);
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        //data.edit().putBoolean("hasBeenSaved", false).apply();
        //data.edit().putBoolean("firstRun", true).apply();
        if (data.getBoolean("firstRun", true))
        {
            Inventory.inventory.add(new Animal("Polar_Bear",6D, 0.5D,1, new Integer[]{}));
            Inventory.inventory.add(new Animal("Cat",2D, 4D,1, new Integer[]{3,6,10}));
            Inventory.inventory.add(new Animal("Chameleon",3D, 3D,1, new Integer[]{}));
            Inventory.inventory.add(new Animal("Chinchilla",2D, 3D,1, new Integer[]{}));
            Inventory.inventory.add(new Animal("Crab",7D, 0D,1, new Integer[]{}));
            Inventory.inventory.add(new Animal("Donkey",5D, 1D,1, new Integer[]{4,7,11}));
            Inventory.inventory.add(new Animal("Fox",3D, 4D,1, new Integer[]{5,8,10}));
            Inventory.inventory.add(new Animal("Jellyfish",1D, 3D,1, new Integer[]{}));
            Inventory.inventory.add(new Animal("Panda",1D, 1D,1, new Integer[]{}));
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
        ChangeScreenButton invButton = new ChangeScreenButton(findViewById(R.id.changeToInventory), InventoryScreen.class, this);
        ChangeScreenButton shopButton = new ChangeScreenButton(findViewById(R.id.open_sounds_button), AudioPlayer.class, this);
        shopButton.button.setOnClickListener(v-> shopButton.clicked());
        invButton.button.setOnClickListener(v -> invButton.clicked());
    }
    @Override
    public void onResume()
    {
        super.onResume();
        for (Inventory.ActiveSlot curSlot : Inventory.activeSlots) {curSlot.startAnim();}
        if (Instant.now().isAfter(Instant.parse(getSharedPreferences("data", MODE_PRIVATE).getString("endPeriod", "null")))) endPeriod();
    }
    @Override
    protected void setBackground()
    {
        dayNightSystem.dayBackground = R.drawable.day_background;
        dayNightSystem.nightBackground = R.drawable.night_background;
        dayNightSystem.background = findViewById(R.id.background_img_main);
    }
    void startIdle()
    {
        if (CurrencyTracker.earnThread != null) {CurrencyTracker.earnThread.stopEarn();}
        CurrencyTracker.earnThread = new EarnThread(this);
        CurrencyTracker.earnThread.start();
    }
    void loadIdle()
    {
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        if (data.getBoolean("firstRun", true)) return;
        if (data.getBoolean("hasBeenSaved", false)) {try {encoding.loadInventory();} catch (Exception e) {}}
    }
    void endPeriod()
    {
        Double averageSleep = ScreenStateReceiver.screenOffTimeTotal/3600/7;
        String message = ("During the week, you slept for " +
                ScreenStateReceiver.screenOffTimeTotal/3600 +
                " hours, thats " + averageSleep + " hours per night on average");
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        ScreenStateReceiver.screenOffTimeTotal = 0;
    }
}
