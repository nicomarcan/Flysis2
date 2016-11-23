package com.example.nmarcantonio.flysys2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FlightsService extends Service {
    static final String TAG = "FlightsService";
    static final String UPDATE_FLIGHTS = "UPDATE_FLIGHTS";
    static final String GET_ALL_FLIGHTS = "GET_ALL_FLIGHTS";
    static final String GET_FLIGHT = "GET_FLIGHT";

    HashMap<String, FlightStatus> flightMap;
    boolean hasStarted = false;
    NotificationManager notificationManager;
    Set<String> flightStrings;
    public FlightsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if (!hasStarted) {
            flightStrings = fetchPreferences();
            initFlightMap(flightStrings);
            hasStarted = true;
            if (intent.getAction() == GET_FLIGHT) {

            }
        }
        else {
            if (intent.getAction() == GET_FLIGHT) {
                //updateFlight(intent.getExtras().getString("flightString"));
            }
            else {

                if (intent.getAction() == UPDATE_FLIGHTS) {
                   // updateFlights(flightStrings);
                }
                else {
                  //  getFlights();
                }
            }
        }
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    Set<String> fetchPreferences() {
        Log.d(TAG, "fetchPreferences: ");
        Set<String> flightSetString;
        SharedPreferences sharedPreferences;
        if ((sharedPreferences = getSharedPreferences("flights", MODE_PRIVATE)) != null) {
            Log.d(TAG, "fetchPreferences: holding shared preferences");
            flightSetString = sharedPreferences.getStringSet("flights", new HashSet<String>());
        }
        else {
            flightSetString = new HashSet<>();
        }
        return flightSetString;
    }

    synchronized void initFlightMap(Set<String> flightStrings) {
        final CountDownLatch cdl = new CountDownLatch(flightStrings.size());
        flightMap = new HashMap<>();
        for (String flightString: flightStrings) {
            String[] strings = flightString.split(",");
            final String airline = strings[0];
            final String number = strings[1];

            new GetFlightInfoTask(new TaskCallback() {
                @Override
                public void callback(String result) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (!obj.has("status")) {
                            return;
                        } else {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<FlightStatus>() {
                            }.getType();

                            String jsonFragment = obj.getString("status");
                            final FlightStatus fi = gson.fromJson(jsonFragment, listType);
                            flightMap.put(airline + "," + number, fi);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        cdl.countDown();
                    }
                }
            }).execute(strings[0], strings[1]);
        }
        try{
            cdl.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "initFlightMap: latch complete");
    }

    void getFlightStatus(String flightString) {
        if (flightMap.containsKey(flightString)) {
            refreshFlight(flightString);
        }
    }

    synchronized void refreshFlight(String flightString) {
        if (flightMap.containsKey(flightString)) {
            String[] strings = flightString.split(",");
            final String airline = strings[0];
            final String number = strings[1];
            new GetFlightInfoTask(new TaskCallback() {
                @Override
                public void callback(String result) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (!obj.has("status")) {
                            return;
                        } else {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<FlightStatus>() {
                            }.getType();

                            String jsonFragment = obj.getString("status");
                            final FlightStatus fi = gson.fromJson(jsonFragment, listType);
                            flightMap.put(airline + "," + number, fi);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ).execute(airline, number);
        }
    }

}
