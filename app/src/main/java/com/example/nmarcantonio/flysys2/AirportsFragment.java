package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by saques on 19/11/16.
 */
public class AirportsFragment extends Fragment implements OnMapReadyCallback  {
    AppCompatActivity context;
    View myView;
    MapView mapView;
    GoogleMap map;
    LocationListener loclistener;
    LocationManager locmanager;
    Location loc;
    String locprovider;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.airports_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_airports);

        mapView = (MapView) myView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity)getActivity();
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Aeropuertos");
        }

        loclistener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locmanager = (LocationManager) context.getSystemService(LOCATION_SERVICE);



        try {
            locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclistener);
            locmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclistener);
            loc = locmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch (SecurityException e){


        }



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        try {
            map.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }


        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {

        }



    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
