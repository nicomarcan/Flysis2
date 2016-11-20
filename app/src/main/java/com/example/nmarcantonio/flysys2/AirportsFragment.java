package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import static android.content.Context.LOCATION_SERVICE;
import static com.example.nmarcantonio.flysys2.R.id.map;

/**
 * Created by saques on 19/11/16.
 */
public class AirportsFragment extends Fragment  {
    final static String AIRPORTS_NAME = "airports";
    final static String CITY_NAME = "city";
    final static String COUNTRY_NAME ="country";
    private AppCompatActivity context;
    private View myView;
    private MapView mapView;
    private GoogleMap map;
    private LocationListener loclistener;
    private LocationManager locmanager;
    private Location loc;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private ArrayList<Airport> airportList;
    private AirportsFragment ap = this;
    private Integer selected;
    private String searchRadius = "100";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.airports_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_airports);


        mapView = (MapView) myView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);


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
                loc = location;
                new HttpGetTask().execute();
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

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }

        locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclistener);
        locmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclistener);
        loc = locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        new HttpGetTask().execute();






    }

    public void afterLocationRequest(){
        try {
            locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclistener);
            locmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclistener);
            loc = locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }catch (SecurityException e){

        }
        new HttpGetTask().execute();
    }


    private class HttpGetTask extends AsyncTask<Void, Void, String> implements OnMapReadyCallback {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getairportsbyposition&" + "" +
                                  "latitude=" + loc.getLatitude() + "&longitude=" + loc.getLongitude() + "&radius=" + searchRadius);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch (Exception exception) {
                exception.printStackTrace();
                return "Unexpected Error";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.has(AirportsFragment.AIRPORTS_NAME))
                    return ;
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Airport>>() {
                }.getType();



                String jsonFragment = obj.getString(AirportsFragment.AIRPORTS_NAME);

                airportList = gson.fromJson(jsonFragment, listType);

                mapView.getMapAsync(this);



            } catch (Exception exception) {

            }
        }

        private String readStream(InputStream inputStream) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int i = inputStream.read();
                while (i != -1) {
                    outputStream.write(i);
                    i = inputStream.read();
                }
                return outputStream.toString();

            } catch (IOException e) {
                return "";
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);



            try {
                MapsInitializer.initialize(ap.getActivity());
            } catch (Exception e) {

            }

            try{
                map.setMyLocationEnabled(true);
            } catch (SecurityException e){

            }


            LatLng lat;
            int i = 0;
            for(Airport  a : airportList){
                lat =   new LatLng(a.getLatitude(),a.getLongitude());
                String splitdesc[] = a.getDescription().split(", ");
                markers.add(i,map.addMarker(new MarkerOptions().position(lat).title(splitdesc[0] + ", " + splitdesc[1]+'\n')));
                map.moveCamera(CameraUpdateFactory.newLatLng(lat));
                i++;
            }
            lat =   new LatLng(loc.getLatitude(),loc.getLongitude());
            float zoomLevel = (float)9.0; //This goes up to 21
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoomLevel));


            final RecyclerView view = (RecyclerView) myView.findViewById(R.id.airport_view);

            if (view != null) {

                AirportAdapter adapter = new AirportAdapter(airportList);
                LinearLayoutManager mLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
                view.setLayoutManager(mLayoutManager);
                view.setAdapter(adapter);


                view.addOnItemTouchListener(new OfferListener(context, view, new ClickListener() {

                    public void onClick(View view, int position) {
                        if(selected!=null){
                            Marker selectedMarker = markers.get(selected);
                            map.addMarker(new MarkerOptions().position(selectedMarker.getPosition()).title(selectedMarker.getTitle()));
                        }
                        selected = position;
                        Airport p = airportList.get(position);
                        LatLng a =   new LatLng(p.getLatitude(),p.getLongitude());
                        markers.get(position).remove();
                        String splitdesc[] = p.getDescription().split(", ");
                        map.addMarker(new MarkerOptions().position(a).title(splitdesc[0] + ", " + splitdesc[1]+'\n').icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        float zoomLevel = (float)11.0; //This goes up to 21
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), zoomLevel));


                    }


                    public void onLongClick(View view, int position) {

                    }
                }));



            }

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
