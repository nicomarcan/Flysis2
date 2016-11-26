package com.example.nmarcantonio.flysys2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Created by traie_000 on 22-Nov-16.
 */

public class AddFlightOnClickListener implements View.OnClickListener {
    private static final String TAG = "AddFlightOnClick";
    private boolean isSubscribed;
    ArrayList<FlightStatus> flights;
    FlightStatus flight;
    FloatingActionButton fab;
    Context context;
    AddFlightOnClickListener(ArrayList<FlightStatus> flights, FlightStatus flight, FloatingActionButton fab, Context context) {
        this.flight = flight;
        this.flights = flights;
        this.fab = fab;
        this.context = context;
        isSubscribed = false;
        setFabUnsubscribed();
        for (FlightStatus f: flights) {
            if (flight.airline.name.equals(f.airline.name) && flight.number == f.number) {
                isSubscribed = true;
                setFabSubscribed();
            }
        }
        fab.show();
    }

    public void setFabSubscribed() {
        fab.setImageResource(R.drawable.ic_clear_black_24dp);
        fab.setColorFilter(0x000000);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffffffff));
    }

    public void setFabUnsubscribed() {
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setColorFilter(0xffffffff);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffcc0000));
    }
    @Override
    public void onClick(View v) {
        if (isSubscribed) {
            isSubscribed = false;
            for(int i = 0; i < flights.size(); i++) {
                FlightStatus f = flights.get(i);
                if (f.airline.name.equals(flight.airline.name) && f.number == flight.number) {
                    flights.remove(i);
                    break;
                }
            }
            PreferencesHelper.updatePreferences(flights, context);
            setFabUnsubscribed();
        }
        else {
            BinPreferencesHelper.deleteFlight(new FlightShort(flight.airline.id,flight.number,flight.airline,
                    flight.departure.airport,flight.arrival.airport,flight),context);
            isSubscribed = true;
            flights.add(flight);
            PreferencesHelper.updatePreferences(flights, context);
            setFabSubscribed();
        }
    }
}
