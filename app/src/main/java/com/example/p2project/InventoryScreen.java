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
        insertButtons();
        Inventory.updateInventory("inv_background");
    }
    @Override
    public void onResume()
    {
        super.onResume();
        dayNightSystem.background = findViewById(R.id.backgroundImg);
    }
}