package com.example.p2project.IdleGame;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.p2project.AudioPlayer;
import com.example.p2project.HasTreats;

import java.util.Dictionary;
import java.util.HashMap;

public class Stories {
    ImageButton[][] buttons = new ImageButton[3][3];
    public MediaPlayer sound = new MediaPlayer();
    public void assignButtons(AudioPlayer main, Dialog dialog)
    {
        String[] animals = {"fox","donkey","cat"};
        for (int s = 0; s < animals.length; s++)
        {
            for (int i = 0; i<3; i++)
            {
                buttons[s][i] =
                        dialog.findViewById(main.getApplicationContext().getResources().getIdentifier((animals[s] + i),
                                "id", main.getApplicationContext().getPackageName()));
                boolean unlocked = main.getSharedPreferences("unlockedStories", Context.MODE_PRIVATE).getInt(animals[s],0) >= i+1;
                dialog.findViewById(main.getApplicationContext().getResources().getIdentifier(("lock_" + animals[s] + (i+1)), "id",
                        main.getApplicationContext().getPackageName())).setVisibility(unlocked ? GONE : VISIBLE);
                if (unlocked && buttons[s][i] != null)
                {
                    String thisSound = animals[s]+"_story_"+i;
                    buttons[s][i].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v)
                        {
                            Log.d("sound", thisSound);
                            if (sound != null && sound.isPlaying()) {sound.stop(); sound.release();}
                            int soundId = main.getApplicationContext().getResources().getIdentifier((thisSound),"raw",main.getApplicationContext().getPackageName());
                            if (soundId != 0) sound = MediaPlayer.create(main,soundId);
                            sound.start();
                            main.updateUI();
                            dialog.dismiss();
                        }
                    });
                }
            }
        }
    }
    void insertStoryButton()
    {

    }
}
