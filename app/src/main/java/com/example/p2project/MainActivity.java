package com.example.p2project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.IdleGame.Inventory.ActiveSlot;
import com.example.p2project.IdleGame.Inventory.InventorySlot;

public class MainActivity extends AppCompatActivity {
    static Boolean invStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //PUT STUFF BENEATH THIS!!!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            if (!invStarted)
            {
                InsertButtons();
                Inventory.inventory.add(new Animal("Bear",1000f));
                Inventory.inventory.add(new Animal("Cat",1000f));
                Inventory.inventory.add(new Animal("Chameleon",1000f));
                Inventory.inventory.add(new Animal("Chinchilla",1000f));
                Inventory.inventory.add(new Animal("Crab",1000f));
                Inventory.inventory.add(new Animal("Donkey",1000f));
                Inventory.inventory.add(new Animal("Fox",1000f));
                Inventory.inventory.add(new Animal("Jellyfish",1000f));
                Inventory.inventory.add(new Animal("Panda",1000f));
                Inventory.UpdateInventory();
                invStarted = true;
            }
            return insets;
        });
        DayNightSystem dayNightSystem = new DayNightSystem();
        dayNightSystem.background = findViewById(R.id.backgroundImg);
        dayNightSystem.Time();
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