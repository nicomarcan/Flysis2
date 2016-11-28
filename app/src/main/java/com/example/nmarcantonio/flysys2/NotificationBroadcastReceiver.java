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
            if (flightStatus == null) {
                return;
            }
            flightStatus.setDescription(context);
            /*
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
            */

            FlightStatus old = PreferencesHelper.getFlight(flightStatus, context);
            PreferencesHelper.updatePreferences(flightStatus, context);
            if (old == null) {
                return;
            }
            if (isNotificationNecessary(old, flightStatus)) {

                Intent resultIntent = new Intent(context, FlightActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", flightStatus.airline.id);
                bundle.putString("number", String.valueOf(flightStatus.number));
                resultIntent.putExtras(bundle);
                /*
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                */
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(FlightActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                String flightKey = flightStatus.airline.id + "|" + String.valueOf(flightStatus.number);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(flightKey.hashCode(), PendingIntent.FLAG_ONE_SHOT);
                String statusString ;
                String gateString = "";
                switch (flightStatus.flightStatusDescription.state) {
                    case SCHEDULED:
                        statusString = context.getResources().getString(R.string.flight_info_status_scheduled);
                        if (flightStatus.departure.airport.terminal != null && flightStatus.departure.airport.gate != null) {
                            gateString = flightStatus.departure.airport.terminal + " " + flightStatus.departure.airport.gate;
                        }
                        break;
                    case BOARDING:
                        statusString = context.getResources().getString(R.string.flight_info_status_boarding);
                        if (flightStatus.departure.airport.terminal != null && flightStatus.departure.airport.gate != null) {
                            gateString = flightStatus.departure.airport.terminal + " " + flightStatus.departure.airport.gate;
                        }
                        break;
                    case FLYING:
                        statusString = context.getResources().getString(R.string.flight_info_status_flying);
                        if (flightStatus.arrival.airport.terminal != null && flightStatus.arrival.airport.gate != null) {
                            gateString = flightStatus.arrival.airport.terminal + " " + flightStatus.arrival.airport.gate;
                        }
                        break;
                    case DIVERT:
                        statusString = context.getResources().getString(R.string.flight_info_status_divert);
                        if (flightStatus.arrival.airport.terminal != null && flightStatus.arrival.airport.gate != null) {
                            gateString = flightStatus.arrival.airport.terminal + " " + flightStatus.arrival.airport.gate;
                        }
                        break;
                    case CANCELLED:
                        statusString = context.getResources().getString(R.string.flight_info_status_cancelled);
                        gateString = "";
                        break;
                    case LANDED:
                        statusString = context.getResources().getString(R.string.flight_info_status_landed);
                        gateString = "";
                        break;
                    default:
                        statusString = context.getResources().getString(R.string.flight_info_status_unknown);
                        gateString = "";
                        break;

                }
                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_flight_black_24dp)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setContentTitle(context.getString(R.string.flight) + " " + flightStatus.airline.id)
                        .setTicker(context.getString(R.string.flight) + " " + flightStatus.airline.id + " " + statusString)
                        .setColor(context.getResources().getColor(R.color.colorPrimaryDark))
                        .setContentText(statusString + " - " + flightStatus.flightStatusDescription.buildDescription(new Date(), context))
                        .setSubText(gateString)
                        .build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notification.contentIntent = pendingIntent;
                notificationManager.notify("com.example.nmarcantonio.flysis2", flightKey.hashCode(), notification);
            }
        }
    }

    private boolean isNotificationNecessary(FlightStatus oldFlightStatus, FlightStatus newFlightStatus) {
        if (oldFlightStatus.flightStatusDescription == null || newFlightStatus.flightStatusDescription == null) {
            return true;
        }
        if (!oldFlightStatus.flightStatusDescription.state.equals(newFlightStatus.flightStatusDescription.state)) {
            return true;
        }
        return !oldFlightStatus.flightStatusDescription.timeInterval.equals(newFlightStatus.flightStatusDescription.timeInterval);

    }
}
