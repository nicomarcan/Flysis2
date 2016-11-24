package com.example.nmarcantonio.flysys2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationBroadcast";
    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(FlightsIntentService.FLIGHT_STATUS)) {
            FlightStatus flightStatus = (FlightStatus) intent.getExtras().get(FlightsIntentService.FLIGHT_STATUS);
            flightStatus.setDescription();

            Intent resultIntent = new Intent(context, FlightActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", "AR");
            bundle.putString("number", "5260");
            resultIntent.putExtras(bundle);
            /*
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            */

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(FlightActivity.class);
            stackBuilder.addNextIntent(resultIntent);


            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

            /*
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
            */
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_flight_black_24dp)
                    .setContentTitle("Vuelo "+ flightStatus.airline.id + " " + String.valueOf(flightStatus.number))
                    .setContentText(flightStatus.flightStatusDescription.buildDescription(new Date()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String flightKey = flightStatus.airline.id + "|" + String.valueOf(flightStatus.number);
            notification.contentIntent = pendingIntent;
            notificationManager.notify("com.example.nmarcantonio.flysis2",0, notification);
        }
    }
}
