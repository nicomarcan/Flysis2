package com.example.nmarcantonio.flysys2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by traie_000 on 22-Nov-16.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    String TAG = "AlarmNotification";
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<FlightStatus> flights = PreferencesHelper.getFlights(context);
        for (FlightStatus flight: flights) {
            Intent i = new Intent(context, FlightsIntentService.class);
            i.setAction(FlightsIntentService.GET_FLIGHT);
            i.putExtra(FlightsIntentService.AIRLINE, flight.airline.id);
            i.putExtra(FlightsIntentService.FLIGHT, String.valueOf(flight.number));
            context.startService(i);
        }
    }
}
