package com.example.p2project;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2project.IdleGame.Animal;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.IdleGame.Inventory.ActiveSlot;
import com.example.p2project.IdleGame.Inventory.InventorySlot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            InsertButtons();
            Inventory.inventory.add(new Animal("evilsauce",1000f));
            Inventory.inventory.add(new Animal("veryevilsauce",1000f));
            Inventory.inventory.add(new Animal("nicesauce",1000f));
            Inventory.UpdateInventory();
            return insets;
        });
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