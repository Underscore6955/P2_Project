package com.example.p2project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.IdleGame.Inventory.ActiveSlot;
import com.example.p2project.IdleGame.Inventory.InventorySlot;

public class InventoryScreen extends HasTreats {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //PUT STUFF BENEATH THIS!!!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inventoryActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        treatView = findViewById(R.id.treat_view);
        earnView = findViewById(R.id.earn_view);
        InsertButtons();
        DayNightSystem dayNightSystem = new DayNightSystem();
        dayNightSystem.background = findViewById(R.id.backgroundImg);
        dayNightSystem.Time();
        Inventory.updateInventory();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        CurrencyTracker.earnThread.main = this;
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