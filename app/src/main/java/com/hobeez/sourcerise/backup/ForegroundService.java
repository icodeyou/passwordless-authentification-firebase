package com.hobeez.sourcerise.backup;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hobeez.sourcerise.R;
import com.hobeez.sourcerise.ui.MainActivity;

import static com.hobeez.sourcerise.backup.ForegroundServiceApplication.CHANNEL_ID;

//*****************************************************************
// Foreground service

//Se lance dans la classe MainActivity avec le code Kotlin suivant :
//Intent serviceIntent = new Intent(activity, ForegroundService.class);
//serviceIntent.putExtra("inputExtra", "MyText");
//startService(serviceIntent);

//Only works with Notification definition in ForegroundServiceApplication.java

//Don't forget to define Service in manifest !


public class ForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This is where everything begins

        String input = intent.getStringExtra("inputExtra");

        Intent notifIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);

        Notification notif = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Title")
                .setContentText(input)
                .setSmallIcon(R.drawable.common_google_signin_btn_text_dark)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notif);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do some stuff
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
