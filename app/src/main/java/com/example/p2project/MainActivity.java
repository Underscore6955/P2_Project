package com.example.p2project;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.EarnThread;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.IdleGame.Inventory.ActiveSlot;
import com.example.p2project.IdleGame.Inventory.InventorySlot;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.Encoding;

import java.io.IOException;
import java.time.*;

public class MainActivity extends AppCompatActivity {
    public TextView treatView;
    public TextView earnView;
    Encoding encoding = new Encoding(this);
    Boolean finishedLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //PUT STUFF BENEATH THIS!!!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        treatView = findViewById(R.id.treat_view);
        earnView = findViewById(R.id.earn_view);
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        /*data.edit().putBoolean("firstRun", true).apply();
        data.edit().putBoolean("hasBeenSaved", false).apply();*/
        InsertButtons();
        if (data.getBoolean("firstRun", true))
        {
            Inventory.inventory.add(new Animal("Bear",1000L, 1L));
            Inventory.inventory.add(new Animal("Cat",1000L, 1L));
            Inventory.inventory.add(new Animal("Chameleon",1000L, 1L));
            Inventory.inventory.add(new Animal("Chinchilla",1000L, 1L));
            Inventory.inventory.add(new Animal("Crab",1000L, 1L));
            Inventory.inventory.add(new Animal("Donkey",1000L, 1L));
            Inventory.inventory.add(new Animal("Fox",1000L, 1L));
            Inventory.inventory.add(new Animal("Jellyfish",1000L, 1L));
            Inventory.inventory.add(new Animal("Panda",1000L, 1L));
            Inventory.UpdateInventory();
            data.edit().putBoolean("firstRun", false).apply();
        }
        DayNightSystem dayNightSystem = new DayNightSystem();
        dayNightSystem.background = findViewById(R.id.backgroundImg);
        dayNightSystem.Time();
        finishedLoading = true;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        if (data.getBoolean("firstRun", true)) return;
        if (data.getBoolean("hasBeenSaved", false)) {try {encoding.loadInventory();} catch (Exception e) {}}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {CurrencyTracker.treats += CurrencyTracker.offlineEarnings(CurrencyTracker.lastOnline);}
        if (CurrencyTracker.earnThread != null) {CurrencyTracker.earnThread.stopEarn();}
        CurrencyTracker.earnThread = new EarnThread(this);
        CurrencyTracker.earnThread.start();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        if (data.getBoolean("firstRun", true)) return;
        if (CurrencyTracker.earnThread != null) {CurrencyTracker.earnThread.stopEarn();}
        try {encoding.saveInventory(Inventory.inventory);} catch (IOException e) {}
        data.edit().putBoolean("hasBeenSaved", true).apply();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {CurrencyTracker.lastOnline = Instant.now();}
    }
void InsertButtons()
{
    for (int i = 0; i <= 11; i++)
    {
        Inventory.inventorySlots[i] = new InventorySlot(i, NewButton("inventory", i));
    }
    for (int i = 0; i < 5; i++)
    {
        Inventory.activeSlots[i] = new ActiveSlot(i, NewButton("active", i));
    }
}
ImageButton NewButton(String type, Integer id)
{
    return findViewById(getResources().getIdentifier((type + id), "id", getPackageName()));
}
}