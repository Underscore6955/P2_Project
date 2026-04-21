package com.example.p2project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Member;

import IdleGame.Animal;
import IdleGame.Inventory;

public class MainActivity extends AppCompatActivity {
public static InventorySlot[] inventorySlots = new InventorySlot[12];
ActiveSlot[] activeSlots = new ActiveSlot[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            InsertButtons();
            return insets;
        });
    }
void InsertButtons()
{
    for (int i = 0; i <= 11; i++)
    {
        inventorySlots[i] = new InventorySlot(i, NewButton("inventory", i));
    }
    for (int i = 0; i <= 5; i++)
    {
        activeSlots[i] = new ActiveSlot(i, NewButton("active", i));
    }
}
ImageButton NewButton(String type, Integer id)
{
    return findViewById(getResources().getIdentifier((type + id), "id", getPackageName()));
}

public class InventorySlot
{
    public Animal content;
    Integer id;
    ImageButton button;
    public InventorySlot(Integer curId, ImageButton curButton)
    {
        id = curId;
        button = curButton;
        curButton.setOnClickListener(new View.OnClickListener()
            {
            public void onClick(View v)
                {
                    Log.d("Press", ("You pressed button " + id));
                }
            }
        );
    }
}
public class ActiveSlot
{
    Animal content;
    Integer id;
    ImageButton button;
    public ActiveSlot(Integer curId, ImageButton curButton)
    {
        id = curId;
        button = curButton;
        curButton.setOnClickListener(new View.OnClickListener()
            {
            public void onClick(View v)
                {
                    Log.d("Press", ("You pressed button " + id));
                }
            }
        );
    }
}
}