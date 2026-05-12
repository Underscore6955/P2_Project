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
import com.example.p2project.IdleGame.ShopScreen;
import com.example.p2project.main_screen.ChangeScreenButton;
import com.example.p2project.main_screen.MainScreen;

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
        ChangeScreenButton invButton = new ChangeScreenButton(findViewById(R.id.open_main_button), MainScreen.class, this);
        ChangeScreenButton shopButton = new ChangeScreenButton(findViewById(R.id.open_sounds_button), AudioPlayer.class, this);
        shopButton.button.setOnClickListener(v-> shopButton.clicked());
        invButton.button.setOnClickListener(v -> invButton.clicked());
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    @Override
    protected void setBackground()
    {
        dayNightSystem.dayBackground = R.drawable.inventory_day;
        dayNightSystem.nightBackground = R.drawable.inventory_night;
        dayNightSystem.background = findViewById(R.id.backgroundImg);
    }
}