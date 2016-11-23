package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FlightActivity extends AppCompatActivity{
    private static String TAG = "FlightActivity";

    ArrayList<FlightStatus> flights;
    Map<TextView, Tuple> map = new HashMap<>();
    private Timer timer;
    private TimerTask timerTask;

    public class Tuple {
        String header;
        Date date;

        public Tuple(String header, Date date) {
            this.header = header;
            this.date = date;
        }
    }

    public void addDate(TextView textView, String header, Date date) {
        map.put(textView, new Tuple(header, date));
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: aasdfasd");
        Log.d(TAG, "onResume: a");
        try {
            Log.d(TAG, "onResume: a");
            timer = new Timer();
            timerTask = new UpdateIntervalTimerTask(map, this);
            timer.schedule(timerTask, 0, 1000);
            Log.d(TAG, "onResume: b");
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        /* updateAllFlights(); */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flight_info_activity);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.flight_info_map);
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.flight_info_collapsible_toolbar);
        ctl.setTitle("Vuelo 1");
        ctl.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = (Toolbar) findViewById(R.id.flight_info_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     finish();
                                                 }
                                             }
        );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_flight_fab);
        fab.hide();
        
        final Activity context = this;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                flights = PreferencesHelper.getFlights(context);

                new GetFlightInfoTask(
                        new FlightInfoCallback(findViewById(R.id.flight_info_coordination), mapFragment, context, flights)
                ).execute("AR", "5260");
            }
        };
        new Handler().post(r);

    }
}
