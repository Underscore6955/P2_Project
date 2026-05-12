package com.example.p2project.IdleGame;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.HasTreats;
import com.example.p2project.InventoryScreen;
import com.example.p2project.R;
import com.example.p2project.main_screen.ChangeScreenButton;
import com.example.p2project.main_screen.MainScreen;

public class ShopScreen extends HasTreats
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shop_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ChangeScreenButton invButton = new ChangeScreenButton(findViewById(R.id.changeToInventory), InventoryScreen.class, this, dayNightSystem);
        ChangeScreenButton mainButton = new ChangeScreenButton(findViewById(R.id.open_main_button), MainScreen.class, this, dayNightSystem);
        invButton.button.setOnClickListener(v -> invButton.clicked());
        mainButton.button.setOnClickListener(v -> mainButton.clicked());
    }
}
