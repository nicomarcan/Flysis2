package com.example.nmarcantonio.flysys2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by saques on 25/11/2016.
 */

public class BinFragment extends Fragment {

    private static final String TAG = "BinFragment";

    AppCompatActivity context;
    View myView;
    FlightBinArrayAdapter flightAdapter;
    ArrayList<FlightShort> flights;


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


    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity)getActivity();

        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_bin);
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle(getString(R.string.bin));
        }
        setHasOptionsMenu(true);
        flights = BinPreferencesHelper.getFlightsInBin(context);
        GridView listView = (GridView) myView.findViewById(R.id.flights_list_view);
        flightAdapter = new FlightBinArrayAdapter(context, flights);
        listView.setAdapter(flightAdapter);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchview,menu);
        menu.removeItem(R.id.offer_search);

    }
}
