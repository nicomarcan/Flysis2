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
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
    int updateCount;
    public static  FlightSearchFragment flightSearchFragment;
    public static boolean searching=false;
    public SwipeRefreshLayout swipeRefreshLayout = null;

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
        swipeRefreshLayout = (SwipeRefreshLayout) myView.findViewById(R.id.flights_refresh);
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
        /**
         * Here goes boom
         */
//        PreferencesHelper.updatePreferences(flights, context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity)getActivity();
        getActivity().findViewById(R.id.flights_empty_error).setVisibility(View.GONE);

        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_flights);
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle(R.string.title_activity_flights);
        }
        setHasOptionsMenu(true);
        flights = PreferencesHelper.getFlights(context);
        GridView listView = (GridView) myView.findViewById(R.id.flights_list_view);
        flightAdapter = new FlightStatusArrayAdapter(context, flights, swipeRefreshLayout);
        listView.setAdapter(flightAdapter);

        if(flights.size() == 0){
            getActivity().findViewById(R.id.flights_empty_error).setVisibility(View.VISIBLE);
        }

        if(savedInstanceState != null && savedInstanceState.getBoolean("searching")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            flightSearchFragment = new FlightSearchFragment();
            fragmentManager.beginTransaction().add(R.id.frame_search, flightSearchFragment).commit();
            searching = true;
        }
    }

    public void updateFlightStatus(final Bundle bundle) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FlightStatus flightStatus = (FlightStatus) bundle.get(FlightsIntentService.FLIGHT_STATUS);

//                flights.set(flights.indexOf(flightStatus), flightStatus);

                /**
                 * Es posible que la instancia de flightStatus que se quiere modificar
                 * no sea la misma que esta en el array, por eso hay que chequear.
                 */
                for(int i=0; i<flights.size() ; i++){
                    FlightStatus f = flights.get(i);
                    if(f.airline.id.equals(flightStatus.airline.getId()) && f.number == flightStatus.number){
                        flights.set(i,flightStatus);
                        if (countDownLatch != null) {
                            countDownLatch.countDown();
                        }
                        else {
                            if (updateCount == flights.size()) {
                                updateCount = 0;
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        flightAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            else {
                                updateCount++;
                            }
                        }
                    }
                }
//                PreferencesHelper.updatePreferences(flights, context);

            }
        };

        Thread thread = new Thread(r);
        thread.start();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.flights_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.action_search_flight){
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            android.app.FragmentManager fragmentManager = getFragmentManager();
             flightSearchFragment = new FlightSearchFragment();
            fragmentManager.beginTransaction().add(R.id.frame_search,flightSearchFragment).commit();
            searching=true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("searching",searching);
        super.onSaveInstanceState(outState);
    }
}

