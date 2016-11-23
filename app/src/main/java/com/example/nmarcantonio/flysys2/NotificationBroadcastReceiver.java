package com.example.nmarcantonio.flysys2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationBroadcast";
    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ksksks");
        if (intent != null && intent.hasExtra(FlightsIntentService.FLIGHT_STATUS)) {
            FlightStatus flightStatus = (FlightStatus) intent.getExtras().get(FlightsIntentService.FLIGHT_STATUS);

            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_flight_black_24dp)
                    .setContentTitle("Vuelo "+ String.valueOf(flightStatus.number))
                    .setContentText("Despega en 20 minutos")
                    .build();
            Log.d(TAG, "onReceive: ksksks");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify("hhhh", 1, notification);
        }
    }
}
