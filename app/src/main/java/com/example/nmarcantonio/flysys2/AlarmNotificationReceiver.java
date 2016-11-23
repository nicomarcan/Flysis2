package com.example.nmarcantonio.flysys2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by traie_000 on 22-Nov-16.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {
    String TAG = "AlarmNotification";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Intent i = new Intent(context, FlightsIntentService.class);
        i.setAction(FlightsIntentService.GET_FLIGHT);
        i.putExtra(FlightsIntentService.AIRLINE, "IB");
        i.putExtra(FlightsIntentService.FLIGHT, "3688");
        context.startService(i);
    }
}
