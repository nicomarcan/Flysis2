package com.example.nmarcantonio.flysys2;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.icu.util.DateInterval;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NotificationCompat;
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

public class GetFlightInfoTask extends AsyncTask<String, Void, String>{
    TaskCallback callback;
    private static final String TAG = "GetFlightInfoTask";

    GetFlightInfoTask(TaskCallback callback) {
        super();
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... params) {
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
            conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return readStream(in);
        } catch (Exception e) {
            /* connection failed */
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.callback(result);
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
            return null;
        }
    }


}
