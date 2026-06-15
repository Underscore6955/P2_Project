package com.example.p2project.IdleGame;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.p2project.HasTreats;

public class Animal
{
    String name;
    Double production, passive;
    Integer level = 1;
    Double xp = 0D;
    Integer[] levels;
    public Animal (String name, Double production, Double passive, Integer level, Integer[] levels)
    {
        this.name = name;
        this.production = production;
        this.passive = passive;
        this.level = level;
        this.levels = levels;
    }
    public void giveXp(Double xp, HasTreats main)
    {
        this.xp += xp;
        while (this.xp >= 100D)
        {
            this.xp -= 100D;
            level += 1;
            production *= 1.1;
            passive *= 1.1;
            for (int i = 0; i < levels.length; i++)
            {
                if (level >= levels[i]) break;
                SharedPreferences prefs = main.getSharedPreferences("unlockedStories", MODE_PRIVATE);
                prefs.edit().putInt(name.toLowerCase(), Math.max(i, prefs.getInt(name.toLowerCase(), 0))).apply();
            }
        }
    }
}
