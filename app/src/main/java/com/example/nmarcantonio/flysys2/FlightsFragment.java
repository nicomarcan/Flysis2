package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by nmarcantonio on 17/11/16.
 */
public class FlightsFragment extends Fragment {
    private static final String TAG = "FlightsFragment";

    AppCompatActivity context;
    View myView;
    ArrayList<FlightStatus> flights;
    FlightStatusArrayAdapter flightAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateFlightStatus(intent.getExtras());
            abortBroadcast();
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.flights_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_flights);


        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FlightsIntentService.ACTION_GET_FLIGHT);
        intentFilter.setPriority(2);
        context.registerReceiver(broadcastReceiver, intentFilter);

        ArrayList<FlightStatus> flightsAux = PreferencesHelper.getFlights(context);
        flights.clear();
        flights.addAll(flightsAux);
        flightAdapter.notifyDataSetChanged();

        for (FlightStatus flight: flights) {
            Intent intent = new Intent(context, FlightsIntentService.class);
            intent.setAction(FlightsIntentService.GET_FLIGHT);
            intent.putExtra(FlightsIntentService.AIRLINE, flight.airline.name);
            intent.putExtra(FlightsIntentService.FLIGHT, String.valueOf(flight.number));
            context.startService(intent);
        }
        /* updateAllFlights(); */
    }
    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity)getActivity();

        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_flights);
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Vuelos");
        }

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.flight_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FlightActivity.class);

                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                startActivity(intent);
            }
        });

        flights = PreferencesHelper.getFlights(context);
        ListView listView = (ListView) myView.findViewById(R.id.flights_list_view);
        flightAdapter = new FlightStatusArrayAdapter(context, flights);
        listView.setAdapter(flightAdapter);
    }

    public void updateFlightStatus(Bundle bundle) {
        FlightStatus flightStatus = (FlightStatus) bundle.get(FlightsIntentService.FLIGHT_STATUS);
        flights.remove(flightStatus);
        flightAdapter.notifyDataSetChanged();
        PreferencesHelper.updatePreferences(flights, context);
    }
}

