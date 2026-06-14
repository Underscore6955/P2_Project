package com.example.p2project.IdleGame;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.p2project.HasTreats;

import java.util.Dictionary;
import java.util.HashMap;

public class Stories {
    public static HashMap<String, Integer> unlockedStories = new HashMap<String, Integer>();
    Button[][] buttons = new Button[3][3];
    public void assignButtons(HasTreats main)
    {
        String[] animals = {"fox","donkey","cat"};
        for (int s = 0; s < animals.length; s++)
        {
            for (int i = 1; i<=3; i++)
            {
                buttons[s][i] = main.findViewById(main.getApplicationContext().getResources().getIdentifier((animals[s] + i), "id", main.getApplicationContext().getPackageName()));
                boolean unlocked = unlockedStories.get(animals[s]) >= i;
                main.findViewById(main.getApplicationContext().getResources().getIdentifier((animals[s] + i + "_lock"), "id", main.getApplicationContext().getPackageName())).setVisibility(unlocked ? GONE : VISIBLE);
                if (unlocked)
                {
                    buttons[s][i].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v)
                        {
                            // play sound
                        }
                    });
                }
            }
        }
    }
}
