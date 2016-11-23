package com.example.nmarcantonio.flysys2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by traie_000 on 22-Nov-16.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    int INTERVAL = 1;
    String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmNotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, 0, i, 0
        );
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INTERVAL,
                60000,
                pi
        );
        Log.d(TAG, "onReceive: ");
    }
}
