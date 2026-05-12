package com.example.p2project.IdleGame;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.p2project.MainActivity;

public class AudioService extends Service {
    private DualSoundManager soundManager;
    private final IBinder binder = new LocalBinder();
    private static final String CHANNEL_ID = "AudioServiceChannel";

    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        soundManager = new DualSoundManager(this);

        soundManager.setTimerListener(new DualSoundManager.TimerListener() {
            @Override
            public void onTimerFinished() {
                stopForeground(true);
                stopSelf();
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Playing Ambient Sounds")
                .setContentText("Your audio is running in the background")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(1, notification);
        return START_STICKY;

    }
    @Override
    public IBinder onBind(Intent intent){
        return binder;
    }
    public DualSoundManager getSoundManager(){
        return soundManager;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (soundManager != null){
            soundManager.releaseAll();
        }
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Background Audio Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
