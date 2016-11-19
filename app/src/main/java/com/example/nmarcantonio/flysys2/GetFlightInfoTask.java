package com.example.nmarcantonio.flysys2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.icu.util.DateInterval;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by traie_000 on 17-Nov-16.
 */

public class GetFlightInfoTask extends AsyncTask<String, Void, String> implements OnMapReadyCallback{

    View flightView;
    GoogleMap map;
    Condition mapCond;
    boolean mapInit;
    Lock lock;
    Context context;

    GetFlightInfoTask(View flightView, MapFragment mapFragment, Context context) {
        super();
        Log.d("hasdfasdf", "aasfdasdf");
        this.flightView = flightView;
        this.map = map;
        mapFragment.getMapAsync(this);
        lock = new ReentrantLock();
        mapCond = lock.newCondition();
        mapInit = false;
        this.context = context;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        lock.lock();
        this.map = map;
        map.getUiSettings().setAllGesturesEnabled(false);
        mapInit = true;
        mapCond.signal();
        lock.unlock();
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... params) {
        Log.d("hasdfasdf2", "aasfdasdf");
        int count = params.length;
        if (count != 2) {
            return null;
        }
        HttpURLConnection conn = null;

        try {
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("hci.it.itba.edu.ar")
                    .appendPath("v1")
                    .appendPath("api")
                    .appendPath("status.groovy")
                    .appendQueryParameter("method", "getflightstatus")
                    .appendQueryParameter("airline_id", params[0])
                    .appendQueryParameter("flight_number", params[1])
                    .build();
            Log.d("uri", uri.toString());
            conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
            Log.d("uri2", conn.toString());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            Log.d("uri3", uri.toString());
            return readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("err", "fallo la conexion");
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        lock.lock();
        Log.d("caca", "onPostExecute: llego");
        try {
            JSONObject obj = new JSONObject(result);
            if (!obj.has("status")) {
                Log.d("return", "error");
                return;
            }
            else {
                Gson gson = new Gson();
                Type listType = new TypeToken<FlightStatus>() {
                }.getType();

                String jsonFragment = obj.getString("status");
                final FlightStatus fi = gson.fromJson(jsonFragment, listType);
                Log.d("airline", fi.airline.name);
                Log.d("number", String.valueOf(fi.number));
                ((TextView)flightView.findViewById(R.id.flight_info_number)).setText("Vuelo " + fi.number);
                ((TextView)flightView.findViewById(R.id.flight_origin_content)).setText(fi.departure.airport.city.getName().split(",")[0]);
                ((TextView)flightView.findViewById(R.id.flight_destination_content_2)).setText(fi.arrival.airport.city.getName().split(",")[0]);
                String statusString;
                String statusDescription = "";
                int statusColor;
                Date arrivalTime;
                Date currentTime = new Date();
                switch (fi.status) {
                    case "L":
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.UK).parse(fi.arrival.actual_time);
                        statusString = "Aterrizado";
                        statusColor = flightView.getResources().getColor(R.color.colorGreen);
                        statusDescription = "Hace" + CustomDateInterval.longInterval(arrivalTime, currentTime);
                        break;
                    case "S":
                        arrivalTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.UK).parse(fi.arrival.scheduled_time);
                        statusString = "Programado";
                        statusColor = flightView.getResources().getColor(R.color.colorGreen);
                        statusDescription = "Despega dentro de" + CustomDateInterval.longInterval(arrivalTime, currentTime);
                        break;
                    case "A":
                        statusString = "En vuelo";
                        statusColor = flightView.getResources().getColor(R.color.colorGreen);
                        break;
                    case "D":
                        statusString = "Desviado";
                        statusColor = flightView.getResources().getColor(R.color.colorRed);
                        break;
                    case "C":
                        statusString = "Cancelado";
                        statusColor = flightView.getResources().getColor(R.color.colorRed);
                        break;
                    default:
                        statusString = "Desconocido";
                        statusColor = flightView.getResources().getColor(R.color.colorRed);
                        break;
                }
                ((TextView)flightView.findViewById(R.id.flight_info_status)).setTextColor(statusColor);
                ((TextView)flightView.findViewById(R.id.flight_info_status)).setText(statusString);
                ((TextView)flightView.findViewById(R.id.flight_info_status_description)).setText(statusDescription);

                while (!mapInit) {
                    try {
                        mapCond.await();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                LatLng departure = new LatLng(fi.departure.airport.city.getLatitude(), fi.departure.airport.city.getLongitude());
                LatLng arrival = new LatLng(fi.arrival.airport.city.getLatitude(), fi.arrival.airport.city.getLongitude());

                Drawable plane = context.getResources().getDrawable(R.drawable.ic_flight_black_24dp);
                Drawable arrow = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
                BitmapDescriptor bd = getMarkerIconFromDrawable(plane);
                BitmapDescriptor ad = getMarkerIconFromDrawable(arrow);
                Marker marker = map.addMarker(new MarkerOptions().position(departure).icon(bd));
                Marker arrowTip = map.addMarker(new MarkerOptions().position(arrival).icon(ad));
                marker.setAnchor(0.49f, 0.5f);
                marker.setRotation((float) SphericalUtil.computeHeading(departure, arrival));
                arrowTip.setAnchor(0.5f, 0.45f);
                arrowTip.setRotation((float) SphericalUtil.computeHeading(departure, arrival));
                map.addPolyline(new PolylineOptions().color(context.getResources().getColor(R.color.colorRed)).add(departure).add(arrival).width(5));
                //marker.setIcon(bd);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(departure).include(arrival);
                LatLngBounds bounds = builder.build();
                //marker.setPosition(arrival);
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                map.moveCamera(cu);

                MarkerAnimation.animateMarkerToHC(marker, arrival, new LatLngInterpolator.LinearFixed());

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d("err", "fallo la conexion");

        }
        lock.unlock();
    }
    private String readStream(InputStream inputStream) {
        Log.d("AAAA", "readStream: llego");
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
