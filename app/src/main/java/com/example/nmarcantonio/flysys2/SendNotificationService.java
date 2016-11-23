package com.example.nmarcantonio.flysys2;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;

import java.util.Set;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendNotificationService extends IntentService {

    public SendNotificationService() {
        super("SendNotificationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Set<FlightStatus> flightStatusSet = (Set<FlightStatus>)intent.getExtras().get("flightStatusSet");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            for (FlightStatus flightStatus: flightStatusSet) {
                Notification not = new Notification.Builder(this)
                        .setContentTitle("chau mundo")
                        .setColor(0xff00ff)
                        .setSmallIcon(R.drawable.ic_flight_black_24dp)
                        .setContentText("aasdkfasdfasd")
                        .build();
                notificationManager.notify("hh", 1, not);
            }
        }
    }

}
