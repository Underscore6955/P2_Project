package com.example.p2project;

import android.content.SharedPreferences;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.Encoding;
import com.example.p2project.IdleGame.Inventory;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;

public class HasTreats extends AppCompatActivity
{
    public TextView treatView;
    public TextView earnView;
    public DayNightSystem dayNightSystem;
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
}
