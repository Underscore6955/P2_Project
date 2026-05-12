package com.example.p2project.IdleGame;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2project.HasTreats;
import com.example.p2project.IdleGame.DayNight.DayNightSystem;
import com.example.p2project.InventoryScreen;

public class EarnThread extends Thread{
    Boolean earning = true;
    public HasTreats main;
    public EarnThread(HasTreats main)
    {
        this.main = main;
    }
    public void run()
    {
        while (earning)
        {
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            if (DayNightSystem.day) CurrencyTracker.treats += CurrencyTracker.curEarn();
            updateText();
        }
    }
    public void updateText()
    {
        main.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                main.treatView.setText(String.valueOf(CurrencyTracker.treats.intValue()));
                main.earnView.setText(String.valueOf(CurrencyTracker.curEarn().intValue() + "/second"));
            }
        });
    }
    public void stopEarn()
    {
        earning = false;
    }
}

