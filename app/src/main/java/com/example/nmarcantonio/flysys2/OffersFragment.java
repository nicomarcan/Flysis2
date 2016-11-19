package com.example.nmarcantonio.flysys2;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

/**
 * Created by nmarcantonio on 17/11/16.
 */

public class OffersFragment extends Fragment {

    View myView;
    final static String DEALS_NAME = "deals";
    final static String CITIES_NAME = "cities";
    private AppCompatActivity context;
    private Location loc;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private City currentCity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.offers_layout, container, false);


        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = (AppCompatActivity) getActivity();
        if (context.getSupportActionBar() != null) {
            context.getSupportActionBar().setTitle("Ofertas");
        }

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.map);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                */

                Intent intent = new Intent(context, OffersMap.class);

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
        });




        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Toast.makeText(context,"HOLAA",Toast.LENGTH_LONG).show();
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
       loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        new GetCityGPS().execute();


    }
    //LLena las fotos y ofertas
    private class HttpGetTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;




            try {

                //URL url= new URL("hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcitiesbyposition&latitude="+loc.getLatitude()+"&longitude="+loc.getLongitude()+"&radius=100");
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getflightdeals&from="+currentCity.getId());
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


                ArrayList<Deal> dealList = gson.fromJson(jsonFragment, listType);
                final ListView listView = (ListView) myView.findViewById(R.id.list_view);
                // Toast.makeText(context,"HOLAAA",Toast.LENGTH_LONG).show();
                if (listView != null) {
                    final Product[] values = new Product[dealList.size()];

                    for (int j = 0; j <dealList.size(); j++) {
                        values[j] = new Product(j, dealList.get(j).getName(), new Double(dealList.get(j).getPrice() ) ,dealList.get(j).getLatitude(),dealList.get(j).getLongitude());
                    }

                    ;

                    ProductArrayAdapter adapter = new ProductArrayAdapter(context  , values);
                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId)
                        {
                            CharSequence text = ((TextView)((RelativeLayout)itemView).getChildAt(1)).getText();
                            Toast.makeText(context, text+" " + position, Toast.LENGTH_LONG).show();
                        }
                    });




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
                    Toast.makeText(context,cityList.get(0).getName(),Toast.LENGTH_LONG).show();
                    new HttpGetTask().execute();
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

}
