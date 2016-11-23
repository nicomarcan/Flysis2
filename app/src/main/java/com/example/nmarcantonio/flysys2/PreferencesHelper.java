package com.example.nmarcantonio.flysys2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by traie_000 on 22-Nov-16.
 */

public class PreferencesHelper {
    private static final String TAG = "PreferencesHelper";

    public static ArrayList<FlightStatus> getFlights(Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FlightStatus>>() {
        }.getType();

        SharedPreferences sharedPreferences = context.getSharedPreferences("flights", Context.MODE_PRIVATE);
        String flightsString;
        if ( (flightsString = sharedPreferences.getString("flights", null)) == null) {
            flightsString = gson.toJson(new ArrayList<FlightStatus>(), listType);
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString("flights", flightsString);
            e.apply();
        }
        ArrayList<FlightStatus> flights = gson.fromJson(flightsString, listType);
        return flights;
    }

    public static void updatePreferences(ArrayList<FlightStatus> list, Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FlightStatus>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences("flights", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String flightsString = gson.toJson(list, listType);
        editor.putString("flights", flightsString);
        editor.apply();
    }

    public static void updatePreferences(FlightStatus flightStatus, Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FlightStatus>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences("flights", Context.MODE_PRIVATE);
        String flightsString = sharedPreferences.getString("flights", null);
        ArrayList<FlightStatus> flights;
        if (flightsString == null) {
            flights = new ArrayList<>();
            flights.add(flightStatus);
        }
        else {
            flights = gson.fromJson(flightsString, listType);
            if (flights.contains(flightStatus)) {
                flights.remove(flightStatus);
                flights.add(flightStatus);
            }
        }
        flightsString = gson.toJson(flights);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("flights", flightsString);
        editor.apply();
    }
}
