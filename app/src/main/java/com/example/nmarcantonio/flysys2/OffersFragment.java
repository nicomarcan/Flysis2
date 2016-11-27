package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by nmarcantonio on 17/11/16.
 */

public class OffersFragment extends Fragment {

    View myView;
    final static String DEALS_NAME = "deals";
    final static String CITIES_NAME = "cities";
    final static String FLIGHTS_NAME ="flights";
    private AppCompatActivity context;
    private Location loc;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private City currentCity;
    private String destId;
    private Double offerPrice;
    public static Integer filter=0;
    private Double ratio;
    private Menu menu;
    private String prevBadge;

    private HashMap<String,Double> stringToRatio = new HashMap<String,Double>();

    private HashMap<String,String> nameToId = new HashMap<>();

    private ArrayList<Deal> dealList = new ArrayList<Deal>();





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.offers_layout, container, false);
        setHasOptionsMenu(true);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (AppCompatActivity) getActivity();
        ((MainActivity)getActivity()).setCurrentSect(R.id.nav_offers);
        if (context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Ofertas");
        }

        final SwipeRefreshLayout l = (SwipeRefreshLayout)getActivity().findViewById(R.id.offer_refresh) ;
        l.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetCityGPS().execute();
            }
        });
        //setHasOptionsMenu(true);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
               // Toast.makeText(context,"HOLAA",Toast.LENGTH_LONG).show();
                //Toast.makeText(context,location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_LONG).show();
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
         mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(context,"HOL",Toast.LENGTH_LONG).show();
            return;
        }//VER QUE ES

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
       if((loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))==null)
           loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(savedInstanceState == null)
         new GetRatiosTask().execute();
        else{

            dealList = (ArrayList<Deal>)savedInstanceState.getSerializable("dealList");
            ratio = (Double)savedInstanceState.getSerializable("ratio");
            stringToRatio = (HashMap<String, Double>)  savedInstanceState.getSerializable("stringToRatio");
            currentCity = (City)savedInstanceState.getSerializable("currentCity");
            nameToId =(HashMap<String, String>) savedInstanceState.getSerializable("nameToId");
            putSameOffers();
        }


    }




    @Override
    public void onResume() {
       super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prevBadge != prefs.getString("money_list","USD")) {
            ratio = stringToRatio.get(prefs.getString("money_list", "USD"));
            prevBadge = prefs.getString("money_list","USD");
            Product[] values = new Product[dealList.size()];
            final GridView listView = (GridView) myView.findViewById(R.id.list_view);
            for (int j = 0; j <dealList.size(); j++) {
                values[j] = new Product(dealList.get(j).getId(), dealList.get(j).getName(), dealList.get(j).getPrice()*ratio  ,dealList.get(j).getLatitude(),dealList.get(j).getLongitude());
                nameToId.put(dealList.get(j).getName().toLowerCase(),dealList.get(j).getId());
            }
            ProductArrayAdapter adapter = new ProductArrayAdapter(context  , values);
            listView.setAdapter(adapter);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_search_offer) {
            Intent intent = new Intent(context, OfferSearch.class);
            intent.putExtra("ratio",ratio);
            intent.putExtra("scrId",currentCity.getId());
            PendingIntent pendingIntent =
                    TaskStackBuilder.create(context)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(intent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent);

            startActivity(intent);
        }
        if(id == R.id.action_map) {
            Intent intent = new Intent(context, OffersMap.class);
            intent.putExtra("ratio",ratio);
            intent.putExtra("srcId",currentCity.getId());
            PendingIntent pendingIntent =
                    TaskStackBuilder.create(context)
                            // add all of DetailsActivity's parents to the stack,
                            // followed by DetailsActivity itself
                            .addNextIntentWithParentStack(intent)
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.offer_fragment_menu,menu);
        this.menu = menu;

    }


    private void putSameOffers(){
        final Product[] values = new Product[dealList.size()];
        final GridView listView = (GridView) myView.findViewById(R.id.list_view);
        for (int j = 0; j <dealList.size(); j++) {
            values[j] = new Product(dealList.get(j).getId(), dealList.get(j).getName(), dealList.get(j).getPrice()*ratio  ,dealList.get(j).getLatitude(),dealList.get(j).getLongitude());
            nameToId.put(dealList.get(j).getName().toLowerCase(),dealList.get(j).getId());
        }
        ProductArrayAdapter adapter = new ProductArrayAdapter(context  , values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId)
            {
                CharSequence text = values[position].getName();


                destId = nameToId.get(text.toString().toLowerCase());
                offerPrice = values[position].getPrice();

                Intent intent = new Intent(context, OfferResults.class);

                intent.putExtra("currentCity", currentCity.getId());
                intent.putExtra("destCity", destId);
                intent.putExtra("offerPrice",offerPrice);
                intent.putExtra("ratio",ratio);
                intent.putExtra("dest",text.toString().split(",")[0]);
                PendingIntent pendingIntent =
                        TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentIntent(pendingIntent);
                startActivity(intent);
                // Toast.makeText(context,currentCity.getId() +" "+ nameToId.get(text), Toast.LENGTH_LONG).show();

            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("dealList",dealList);
        outState.putSerializable("nameToId",nameToId);
        outState.putSerializable("currentCity",currentCity);
        outState.putSerializable("ratio",ratio);
        outState.putSerializable("stringToRatio",stringToRatio);
        super.onSaveInstanceState(outState);
    }


    //LLena las fotos y ofertas
    private class HttpGetOffersTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {

                //URL url= new URL("hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcitiesbyposition&latitude="+loc.getLatitude()+"&longitude="+loc.getLongitude()+"&radius=100");
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getlastminuteflightdeals&from="+currentCity.getId());
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

            //Toast.makeText(context,loc.getLatitude()+" "+loc.getLongitude(),Toast.LENGTH_LONG).show();

            try {
                JSONObject obj = new JSONObject(result);
                if (!obj.has(OffersFragment.DEALS_NAME))
                    return ;
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Deal>>() {
                }.getType();

                String jsonFragment = obj.getString(OffersFragment.DEALS_NAME);


                final GridView listView = (GridView) myView.findViewById(R.id.list_view);
                // Toast.makeText(context,"HOLAAA",Toast.LENGTH_LONG).show();
                if (listView != null) {


                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    ratio = stringToRatio.get(prefs.getString("money_list","USD"));
                    prevBadge= prefs.getString("money_list","USD");
                    dealList = gson.fromJson(jsonFragment, listType);
                    long seed = System.nanoTime();
                    Collections.shuffle(dealList, new Random(seed));
                    final Product[] values = new Product[dealList.size()];

                    for (int j = 0; j <dealList.size(); j++) {
                        values[j] = new Product(dealList.get(j).getId(), dealList.get(j).getName(), dealList.get(j).getPrice()*ratio  ,dealList.get(j).getLatitude(),dealList.get(j).getLongitude());
                        nameToId.put(dealList.get(j).getName().toLowerCase(),dealList.get(j).getId());
                    }



                    ProductArrayAdapter adapter = new ProductArrayAdapter(context  , values);
                    listView.setAdapter(adapter);
                    final SwipeRefreshLayout l = (SwipeRefreshLayout)getActivity().findViewById(R.id.offer_refresh) ;
                    l.setRefreshing(false);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId)
                        {
                            CharSequence text = values[position].getName();


                            destId = nameToId.get(text.toString().toLowerCase());
                            offerPrice = values[position].getPrice();

                            Intent intent = new Intent(context, OfferResults.class);

                            intent.putExtra("currentCity", currentCity.getId());
                            intent.putExtra("destCity", destId);
                            intent.putExtra("offerPrice",offerPrice);
                            intent.putExtra("ratio",ratio);
                            intent.putExtra("dest",text.toString().split(",")[0]);
                            PendingIntent pendingIntent =
                                    TaskStackBuilder.create(context)
                                            // add all of DetailsActivity's parents to the stack,
                                            // followed by DetailsActivity itself
                                            .addNextIntentWithParentStack(intent)
                                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                            builder.setContentIntent(pendingIntent);
                            startActivity(intent);
                           // Toast.makeText(context,currentCity.getId() +" "+ nameToId.get(text), Toast.LENGTH_LONG).show();

                        }
                    });




                }
                ;


            } catch (Exception exception) {
                //  resultTextView.append(new Integer("10").toString());
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
    }





    private class GetCityGPS extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {

                URL url= new URL("http://hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcitiesbyposition&latitude="+loc.getLatitude()+"&longitude="+loc.getLongitude()+"&radius=100");
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
                if (!obj.has(OffersFragment.CITIES_NAME))
                    return ;
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<City>>() {
                }.getType();

                String jsonFragment = obj.getString(OffersFragment.CITIES_NAME);


                ArrayList<City> cityList = gson.fromJson(jsonFragment, listType);
                if(cityList.size()>0){
                    currentCity = cityList.get(0);

                    new HttpGetOffersTask().execute();



                    FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.map);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                */


                        }
                    });
                }



            } catch (Exception exception) {
                //  resultTextView.append(new Integer("10").toString());
            }
            ;
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
    }

    private class GetRatiosTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection conn = null;
            String ret = null, order;
            try {

                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/misc.groovy?method=getcurrencies");
                conn = (HttpURLConnection) new URL(url.toString()).openConnection();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                ret = readStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONObject obj = new JSONObject(result);
                if (!obj.has("currencies")) {
                    return;
                }
                else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Currency>>() {
                    }.getType();

                    String jsonFragment = obj.getString("currencies");


                    ArrayList<Currency> currencies = gson.fromJson(jsonFragment, listType);

                    for (Currency a : currencies) {

                        stringToRatio.put(a.getId(),1/a.getRatio());
                    }

                    new GetCityGPS().execute();

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        private String readStream(InputStream inputStream) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int i = inputStream.read();
                while(i != -1 ) {
                    outputStream.write(i);
                    i = inputStream.read();
                }
                return outputStream.toString();
            } catch(IOException e) {
                e.printStackTrace();
                Log.d("err", "fallo la conexion");
                return null;
            }
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

}
