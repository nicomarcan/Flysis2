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
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by nmarcantonio on 17/11/16.
 */
public class FlightsFragment extends Fragment {
    private static final String TAG = "FlightsFragment";

    AppCompatActivity context;
    View myView;
    ArrayList<FlightStatus> flights;
    FlightStatusArrayAdapter flightAdapter;
    CountDownLatch countDownLatch = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            updateFlightStatus(intent.getExtras());
            abortBroadcast();
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.flights_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_flights);

        final FlightsFragment fragment = this;
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.flights_refresh);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fragment.refresh();
                }
            });
        }

        return myView;
    }

    public void refresh() {
        final Activity activity = getActivity();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                ArrayList<FlightStatus> flightsAux = PreferencesHelper.getFlights(context);
                flights.clear();
                flights.addAll(flightsAux);
                countDownLatch = new CountDownLatch(flights.size());
                for (FlightStatus flight: flights) {
                    Intent intent = new Intent(context, FlightsIntentService.class);
                    intent.setAction(FlightsIntentService.GET_FLIGHT);
                    intent.putExtra(FlightsIntentService.AIRLINE, flight.airline.id);
                    intent.putExtra(FlightsIntentService.FLIGHT, String.valueOf(flight.number));
                    context.startService(intent);
                }
                try {
                    countDownLatch.await(10, TimeUnit.SECONDS);
                }
                catch (InterruptedException e) {

                }
                countDownLatch = null;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.flights_refresh);
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        flightAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FlightsIntentService.ACTION_GET_FLIGHT);
        intentFilter.setPriority(2);
        context.registerReceiver(broadcastReceiver, intentFilter);
        refresh();
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
        setHasOptionsMenu(true);
        flights = PreferencesHelper.getFlights(context);
        ListView listView = (ListView) myView.findViewById(R.id.flights_list_view);
        flightAdapter = new FlightStatusArrayAdapter(context, flights);
        listView.setAdapter(flightAdapter);
    }

    public void updateFlightStatus(final Bundle bundle) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FlightStatus flightStatus = (FlightStatus) bundle.get(FlightsIntentService.FLIGHT_STATUS);
                flights.set(flights.indexOf(flightStatus), flightStatus);
                flightAdapter.notifyDataSetChanged();
                PreferencesHelper.updatePreferences(flights, context);
            }
        };

        Handler handle = new Handler();
        handle.post(r);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.searchview,menu);
        MenuItem searchItem = menu.findItem(R.id.offer_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.split(" ").length!=2){
                    //TODO: Arreglarlo bien esto, #NegradaOta
                    Toast.makeText(context,"NUCLEAR",Toast.LENGTH_LONG).show();
                    return false;
                }
                String id = query.split(" ")[0].toUpperCase();
                String number = query.split(" ")[1];
                Intent intent = new Intent(context, FlightActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("number",number);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}

