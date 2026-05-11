package com.example.p2project.IdleGame;

import android.content.Context;
import android.media.MediaPlayer;
import android.content.SharedPreferences;
import android.media.PlaybackParams;
import android.os.Build;
import java.util.HashSet;
import java.util.Set;
import android.os.CountDownTimer;

public class DualSoundManager {
    private Context context;

    // The two sound players
    private MediaPlayer player1;
    private MediaPlayer player2;

    // Sleep timer
    private CountDownTimer sleepTimer;

    interface TimerListener {
        void onTimerFinished();
    }

    private TimerListener timerListener;

    public void setTimerListener(TimerListener listener) {
        this.timerListener = listener;
    }

    private float currentVolume = 1.0f;
    private float currentPitch = 1.0f;

    private static final String PREFS_NAME = "SoundAppPrefs";
    private static final String FAVORITE_KEY = "FavoriteSounds";
    private SharedPreferences prefs;

    public DualSoundManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    public void PlaySound1(int rawResourceId) {
        removeSound1();

        player1 = MediaPlayer.create(context, rawResourceId);
        if (player1 != null) {
            player1.setLooping(true);
            applyVolumeAndPitch(player1);
            player1.start();
        }
    }

    public void playSound2(int rawResourceId) {
        removeSound2();

        player2 = MediaPlayer.create(context, rawResourceId);
        if (player2 != null) {
            player2.setLooping(true);
            applyVolumeAndPitch(player2);
            player2.start();
        }
    }

    public void pauseAll() {
        if (player1 != null && player1.isPlaying()) player1.pause();
        if (player2 != null && player2.isPlaying()) player2.pause();
    }

    public void resumeAll() {
        if (player1 != null && !player1.isPlaying()) player1.start();
        if (player2 != null && !player2.isPlaying()) player2.start();
    }

    public void setMasterVolume(float volume) {
        this.currentVolume = Math.max(0.0f, Math.min(1.0f, volume));

        if (player1 != null) player1.setVolume(currentVolume, currentVolume);
        if (player2 != null) player2.setVolume(currentVolume, currentVolume);
    }

    public void setMasterPitch(float pitch) {
        this.currentPitch = pitch;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (player1 != null && player1.isPlaying()) {
                PlaybackParams params = new PlaybackParams();
                params.setPitch(currentPitch);
                player1.setPlaybackParams(params);
            }
            if (player2 != null && player2.isPlaying()) {
                PlaybackParams params = new PlaybackParams();
                params.setPitch(currentPitch);
                player2.setPlaybackParams(params);
            }
        }
    }

    private void applyVolumeAndPitch(MediaPlayer player) {
        player.setVolume(currentVolume, currentVolume);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = new PlaybackParams();
            params.setPitch(currentPitch);
            player.setPlaybackParams(params);
        }
    }

    public void toggleFavorite(int rawResourceId) {
        String soundName = context.getResources().getResourceEntryName(rawResourceId);

        Set<String> favorites = new HashSet<>(prefs.getStringSet(FAVORITE_KEY, new HashSet<>()));

        if (favorites.contains(soundName)) {
            favorites.remove(soundName);
        } else {
            favorites.add(soundName);
        }

        prefs.edit().putStringSet(FAVORITE_KEY, favorites).apply();
    }

    public boolean isFavorite(int rawResourceId) {
        String soundName = context.getResources().getResourceEntryName(rawResourceId);
        Set<String> favorites = prefs.getStringSet(FAVORITE_KEY, new HashSet<>());
        return favorites.contains(soundName);
    }


    public void removeSound1() {
        if (player1 != null) {
            if (player1.isPlaying()) {
                player1.stop();
            }
            player1.release();
            player1 = null;
        }
    }

    public void removeSound2() {
        if (player2 != null) {
            if (player2.isPlaying()) {
                player2.stop();
            }
            player2.release();
            player2 = null;
        }
    }

    public void releaseAll() {
        removeSound1();
        removeSound2();
    }

    public boolean isAnyPlaying() {
        boolean p1Playing = player1 !=null && player1.isPlaying();
        boolean p2Playing = player2 !=null && player2.isPlaying();
        return p1Playing || p2Playing;
    }
    public void startSleepTimer(int minutes) {
        cancelSleepTimer();
        long durationInMillis = minutes * 60 * 1000L;

        sleepTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick (long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                pauseAll();
                if (timerListener != null) {
                    timerListener.onTimerFinished();
                }
            }
        }.start();
    }
    public void cancelSleepTimer() {
        if (sleepTimer != null) {
            sleepTimer.cancel();
            sleepTimer = null;
        }
    }
}