package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.phenotype.Configuration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by saques on 19/11/16.
 */
public class AirportsFragment extends Fragment  {
    private static final String LATITUDE_BUND = "Latitude";
    private static final String LONGITUDE_BUND = "Longitude";


    final static String AIRPORTS_NAME = "airports";
    final static String CITIES_NAME = "cities";
    private AppCompatActivity context;
    private View myView;
    private MapView mapView;
    private GoogleMap map;
    private LocationListener loclistener;
    private LocationManager locmanager;
    private Location loc = null;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private ArrayList<Airport> airportList;
    private ArrayList<CityInfo_2> citiesList;
    private Map<String,CityInfo_2> citiesMap;
    private AirportsFragment ap = this;
    private Integer selected;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.airports_layout, container, false);
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_airports);

        if(savedInstanceState != null && savedInstanceState.getBoolean("check")){
            loc = new Location("dummyprovider");
            loc.setLatitude(savedInstanceState.getDouble(LATITUDE_BUND));
            loc.setLongitude(savedInstanceState.getDouble(LONGITUDE_BUND));
        }

        mapView = (MapView) myView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);



        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.searchview,menu);
        MenuItem searchItem = menu.findItem(R.id.offer_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!citiesMap.containsKey(query)) return true;
                loc.setLongitude(citiesMap.get(query).getLongitude());
                loc.setLatitude(citiesMap.get(query).getLatitude());
                new GetNearbyAirportsAsync().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        new GetCitiesAsync(context,searchView).execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = (AppCompatActivity)getActivity();
        if(context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Aeropuertos");
        }





        locmanager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }

        if(loc == null) {
            loc = locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = locmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (loc == null){
                loc = new Location("dummyprovider");
                loc.setLongitude(-58.381592);
                loc.setLatitude(-34.603722);
            }
        }

        new GetNearbyAirportsAsync().execute();


    }

    public void afterLocationRequest(){
        try {
            if(loc == null) {
                loc = locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc == null) {
                    loc = locmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (loc == null){
                    loc = new Location("dummyprovider");
                    loc.setLongitude(-58.381592);
                    loc.setLatitude(-34.603722);
                }
            }
            new GetNearbyAirportsAsync().execute();
        }catch (SecurityException e){

        }
    }




    private class GetCitiesAsync extends AsyncTask<Void, Void, String>{

        Context context;
        SearchView searchView;

        public GetCitiesAsync(Context context, SearchView searchView){
            this.context = context;
            this.searchView = searchView;
        }


        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcities&" +"page_size=1000");
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
        protected void onPostExecute(String result) {

            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.has(AirportsFragment.CITIES_NAME)) {
                    return;
                }
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<CityInfo_2>>() {
                }.getType();



                String jsonFragment = obj.getString(AirportsFragment.CITIES_NAME);

                citiesList = gson.fromJson(jsonFragment, listType);

                citiesMap = new HashMap<>();


                for(CityInfo_2 c : citiesList){
                    citiesMap.put(c.getName().split(", ")[0],c);
                }

                final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)     searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchAutoComplete.setTextColor(Color.WHITE);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.autocomplete_layout, new ArrayList<>(citiesMap.keySet()));
                searchAutoComplete.setAdapter(adapter);

                SearchManager searchManager =
                        (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
                searchView.setSearchableInfo(
                        searchManager.getSearchableInfo(getActivity().getComponentName()));

                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionSelect(int position) {
                        return false;
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        searchView.setQuery(adapter.getItem(position), false);
                        return true;
                    }
                });



            } catch (Exception exception) {

            }
        }



    }


    private class GetNearbyAirportsAsync extends AsyncTask<Void, Void, String> implements OnMapReadyCallback {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getairportsbyposition&" + "" +
                                  "latitude=" + loc.getLatitude() + "&longitude=" + loc.getLongitude() + "&radius=" + FlysysSettings.rangeSetting);
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
            map.getUiSettings().setMyLocationButtonEnabled(true);

            for(Marker m : markers){
                m.remove();
            }

            markers = new ArrayList<>();

            LatLng locll = new LatLng(loc.getLatitude(),loc.getLongitude());

            markers.add(0,map.addMarker(new MarkerOptions().position(locll).title(getString(R.string.you)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));



            try {
                MapsInitializer.initialize(ap.getActivity());
            } catch (Exception e) {

            }

            LatLng lat;
            int i = 1;
            for(Airport  a : airportList){
                Location locb = new Location(loc);
                locb.setLatitude(a.getLatitude());
                locb.setLongitude(a.getLongitude());
                lat =   new LatLng(a.getLatitude(),a.getLongitude());
                String splitdesc[] = a.getDescription().split(", ");
                String snippet = getString(R.string.airport_distance) +": ";
                switch (FlysysSettings.distanceSetting){
                    case METRIC:
                        snippet += Math.round(loc.distanceTo(locb)/1000) + " km";
                        break;
                    case IMPERIAL:
                        snippet += (int)Math.round(loc.distanceTo(locb)/1609.34) + " mi";
                        break;
                    default:
                        break;
                }
                markers.add(i,map.addMarker(new MarkerOptions().position(lat).title(splitdesc[0] + ", " + splitdesc[1]+'\n').snippet(snippet)));
                map.moveCamera(CameraUpdateFactory.newLatLng(lat));
                i++;
            }
            lat =   new LatLng(loc.getLatitude(),loc.getLongitude());
            float zoomLevel = (float)9.0; //This goes up to 21
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, zoomLevel));




        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null){
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null) {
            mapView.onDestroy();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(loc != null) {
            outState.putBoolean("check",true);
            outState.putDouble(LATITUDE_BUND, loc.getLatitude());
            outState.putDouble(LONGITUDE_BUND, loc.getLongitude());
        } else {
            outState.putBoolean("check",false);
        }
        if(mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapView != null) {
            mapView.onLowMemory();
        }
    }



}
