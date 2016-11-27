package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class UpdateIntervalTimerTask extends TimerTask {
    private static final String TAG = "UpdateIntervalTimer";
    Map<TextView, FlightActivity.Tuple> map;

    Activity context;

    public UpdateIntervalTimerTask(Map<TextView, FlightActivity.Tuple> map, Activity context) {
        this.map = map;
        this.context = context;
    }

    @Override
    public void run() {
        /*
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<TextView, FlightActivity.Tuple> e: map.entrySet()) {
                    TextView textView = e.getKey();
                    FlightActivity.Tuple tuple = e.getValue();
                    Date currentDate = new Date();
                    String dateString = CustomDateInterval.longInterval(tuple.date,  currentDate);
                    textView.setText(tuple.header + dateString);
                }
            }
        });
        */
    }
}
