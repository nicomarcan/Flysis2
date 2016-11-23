package com.example.nmarcantonio.flysys2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;


public class FlightsIntentService extends IntentService {
    public static final String GET_FLIGHT = "GET_FLIGHT";
    public static final String FLIGHT = "FLIGHT";
    public static final String AIRLINE = "AIRLINE";
    public static final String FLIGHT_STATUS = "FLIGHT_STATUS";
    public static final String CALLBACK = "CALLBACK";
    private static final String TAG = "FlightsIntentService";
    public static String ACTION_GET_FLIGHT = "com.example.nmarcantonio.flysis2.ACTION_GET_FLIGHT";

    public FlightsIntentService() {
        super("FlightsIntentService");
    }


    public static void startGetFlight(Context context, String airline, String flight) {
        Intent intent = new Intent(context, FlightsIntentService.class);
        intent.setAction(GET_FLIGHT);
        intent.putExtra(AIRLINE, airline);
        intent.putExtra(FLIGHT, flight);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_FLIGHT.equals(action)) {
                final String airline = intent.getStringExtra(AIRLINE);
                final String flight = intent.getStringExtra(FLIGHT);
                handleGetFlight(airline, flight);
                
            }
        }
    }

    private void handleGetFlight(final String airline, final String flight) {
        new GetFlightInfoTask(new TaskCallback() {
            @Override
            public void callback(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    if (!obj.has("status")) {
                        Log.d(TAG, "callback: failed");
                        return;
                    } else {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<FlightStatus>() {
                        }.getType();

                        String jsonFragment = obj.getString("status");
                        final FlightStatus fi = gson.fromJson(jsonFragment, listType);
                        Intent intent = new Intent(ACTION_GET_FLIGHT);
                        intent.putExtra(FLIGHT_STATUS, fi);
                        sendOrderedBroadcast(intent, null);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).execute(airline, flight);
    }
}
