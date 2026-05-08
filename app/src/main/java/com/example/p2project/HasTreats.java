package com.example.p2project;

import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.IdleGame.Encoding;
import com.example.p2project.IdleGame.Inventory;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;

public class HasTreats extends AppCompatActivity
{
    public TextView treatView;
    public TextView earnView;
    protected Encoding encoding = new Encoding(this);
    public void onPause()
    {
        super.onPause();
        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
        try {encoding.saveInventory(Inventory.inventory);} catch (IOException e) {}
        data.edit().putBoolean("hasBeenSaved", true).apply();
        data.edit().putString("lastOnline", Instant.now().toString()).apply();
    }
}
