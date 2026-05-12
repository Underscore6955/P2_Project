package com.example.p2project.main_screen;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.HasTreats;
import com.example.p2project.IdleGame.CurrencyTracker;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.IdleGame.EarnThread;
import com.example.p2project.IdleGame.Inventory;
import com.example.p2project.InventoryScreen;


public class ChangeScreenButton
{
    public Button button;
    Class<?> changeScreen;
    AppCompatActivity thisScreen;
    public ChangeScreenButton(Button button, Class<?> changeScreen, HasTreats thisScreen)
    {
        this.button = button;
        this.changeScreen = changeScreen;
        this.thisScreen = thisScreen;
        button.setOnClickListener(v -> clicked());
    }
    public void clicked()
    {
        Intent intent = new Intent(thisScreen, changeScreen);
        thisScreen.startActivity(intent);
    }
}
