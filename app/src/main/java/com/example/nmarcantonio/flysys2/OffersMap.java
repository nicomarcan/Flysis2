package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.List;

/**
 * Created by nmarcantonio on 17/11/16.
 */
public class OffersMap extends AppCompatActivity  {


    private GoogleMap mMap;
    private Double maxPrice=Double.MIN_VALUE;
    private Double minPrice=Double.MAX_VALUE;
    private Double midPrice;

    View myView;
    final static String DEALS_NAME = "deals";
    private Activity context;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> values;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private Integer selected;
    private double ratio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_map);
        context=this;
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mapa");
        }
        new HttpGetTask().execute();

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class HttpGetTask extends AsyncTask<Void, Void, String> implements OnMapReadyCallback {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getlastminuteflightdeals&from=BUE");
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
                if (!obj.has(MainActivity.DEALS_NAME))
                    return ;
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Deal>>() {
                }.getType();

                String jsonFragment = obj.getString(MainActivity.DEALS_NAME);


                ArrayList<Deal> dealList = gson.fromJson(jsonFragment, listType);
                final RecyclerView view = (RecyclerView) findViewById(R.id.offer_view);

                if (view != null) {
                    values = new ArrayList<Product>();

                    for (int j = 0; j <dealList.size(); j++) {
                        ratio = getIntent().getDoubleExtra("ratio",1);
                        double price = dealList.get(j).getPrice()*ratio;
                        values.add (j, new Product(j, dealList.get(j).getName(), price,dealList.get(j).getLatitude(),dealList.get(j).getLongitude() ));
                        if(price < minPrice)
                            minPrice = price;
                        if(price > maxPrice)
                            maxPrice = price;
                    }
                    midPrice = (maxPrice + minPrice)/2;

                    OfferAdapter adapter = new OfferAdapter(values,context);
                    mLayoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
                    view.setLayoutManager(mLayoutManager);
                   view.setAdapter(adapter);

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);



                    view.addOnItemTouchListener(new OfferListener(getApplicationContext(), view, new ClickListener() {

                        public void onClick(View view, int position) {

                            //Toast.makeText(getApplicationContext(), product.getName() + " is selected!", Toast.LENGTH_LONG).show();
                            Product p = values.get(position);
                            float zoomLevel = (float)3.0; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), zoomLevel));


                        }


                        public void onLongClick(View view, int position) {

                        }
                    }));



                }
                ;


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

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Add a marker in Sydney and move the camera
            LatLng a;
            int i = 0;
            for(Product p : values){
                a =   new LatLng(p.getLatitude(),p.getLongitude());
                float color;
                if(p.getPrice() > (midPrice+maxPrice)/2)
                    color = BitmapDescriptorFactory.HUE_RED;
                else if(p.getPrice() < (midPrice+minPrice)/2)
                    color = BitmapDescriptorFactory.HUE_GREEN;
                else
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                markers.add(i,mMap.addMarker(new MarkerOptions().position(a).title(p.getName().split(",")[0]+'\n'+p.getPrice()).icon(BitmapDescriptorFactory.defaultMarker(color))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(a));
                i++;
            }
                Product p = values.get(0);

            float zoomLevel = (float)3.0; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitude(),p.getLongitude()), zoomLevel));
            ((TextView)findViewById(R.id.max_price)).setText(new Integer(maxPrice.intValue()).toString());
            ((TextView)findViewById(R.id.mid_price)).setText(new Integer(midPrice.intValue()).toString());
            ((TextView)findViewById(R.id.min_price)).setText(new Integer(minPrice.intValue()).toString());



        }
    }






}