package com.example.nmarcantonio.flysys2;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by saques on 25/11/2016.
 */

public class BinPreferencesHelper {
    private static final String TAG = "BinPreferencesHelper";
    private static FlightBinArrayAdapter adapter = null;

    public static void registerFlightBinAdapter(FlightBinArrayAdapter a){
        adapter = a;
    }

    public static ArrayList<FlightShort> getFlightsInBin(Context context) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FlightShort>>() {
        }.getType();

        SharedPreferences sharedPreferences = context.getSharedPreferences("flightsBin", Context.MODE_PRIVATE);
        String flightsString;
        if ( (flightsString = sharedPreferences.getString("flightsBin", null)) == null) {
            flightsString = gson.toJson(new ArrayList<FlightShort>(), listType);
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString("flightsBin", flightsString);
            e.apply();
        }
        ArrayList<FlightShort> flights = gson.fromJson(flightsString, listType);
        return flights;
    }

    private static void updatePreferences(ArrayList<FlightShort> list, Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<FlightShort>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences("flightsBin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String flightsString = gson.toJson(list, listType);
        editor.putString("flightsBin", flightsString);
        editor.apply();
        if(adapter != null) {
            adapter.update();
        }
    }

    public static boolean recycleFlight(FlightShort flight, Context context){
        if(!isRecycled(flight,context)){
            ArrayList<FlightShort> shorts = getFlightsInBin(context);
            shorts.add(flight);
            updatePreferences(shorts,context);
            return true;
        }
        return false;
    }

    public static boolean isRecycled(FlightShort flight, Context context){
        ArrayList<FlightShort> shorts = getFlightsInBin(context);
        for(FlightShort s : shorts){
            if(s.getId().equals(flight.getId()) && s.getNumber()==flight.getNumber()){
                return true;
            }
        }
        return false;
    }

    public static boolean deleteFlight(FlightShort flight, Context context ){
        if(isRecycled(flight,context)){
            ArrayList<FlightShort> shorts = getFlightsInBin(context);
            for(int i=0 ; i<shorts.size() ; i++){
                FlightShort s = shorts.get(i);
                if(s.getId().equals(flight.getId()) && s.getNumber()==flight.getNumber()){
                    shorts.remove(i);
                    break;
                }
            }
            if(shorts.size() == 0)
                    ((Activity)context).findViewById(R.id.bin_empty_error).setVisibility(View.VISIBLE);
            updatePreferences(shorts,context);
            return true;
        }
        return false;
    }

}

