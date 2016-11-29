package com.example.nmarcantonio.flysys2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by aa on 11/24/2016.
 */

public class PutFlightCommentTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "PutFlightCommentTask";
    View commentView;
    FlightActivity context;

    public PutFlightCommentTask(View commentView,FlightActivity context){
        this.commentView = commentView;
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn = null;
        String ret = null;
        try {
            if (params.length != 10) {
                throw new IllegalArgumentException();
            }

            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected())
                return null;

            JSONObject ratingsJson = new JSONObject();
            ratingsJson.put("friendliness", Integer.valueOf(params[2]));
            ratingsJson.put("food", Integer.valueOf(params[3]));
            ratingsJson.put("punctuality", Integer.valueOf(params[4]));
            ratingsJson.put("mileage_program", Integer.valueOf(params[5]));
            ratingsJson.put("comfort", Integer.valueOf(params[6]));
            ratingsJson.put("quality_price", Integer.valueOf(params[7]));
            JSONObject airlineJson = new JSONObject();
            airlineJson.put("id", params[0]);
            JSONObject flightJson = new JSONObject();
            flightJson.put("airline", airlineJson);
            flightJson.put("number", Integer.valueOf(params[1]));
            JSONObject requestJson = new JSONObject();
            requestJson.put("flight", flightJson);
            requestJson.put("rating", ratingsJson);
            requestJson.put("yes_recommend", Boolean.valueOf(params[8]));
            requestJson.put("comments", Uri.encode(StringEscapeUtils.escapeHtml4(params[9])));
            String encodedJson = URLEncoder.encode(requestJson.toString(), "UTF-8");

            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("hci.it.itba.edu.ar")
                    .appendPath("v1")
                    .appendPath("api")
                    .appendPath("review.groovy")
                    .appendQueryParameter("method", "reviewairline2")
                    .appendQueryParameter("review", encodedJson)
                    .build();

            conn = (HttpURLConnection) new URL(uri.toString()).openConnection();

            int httpResult = conn.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                InputStream br = conn.getInputStream();
                ret = readStream(br);
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        try {

            if(result == null) {
                Toast.makeText(context, R.string.no_internet_msg_comment, Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject object = new JSONObject(result);
            if (object.has("review") && object.getBoolean("review")) {
                Log.d(TAG, "onPostExecute: true");
                Toast.makeText(context, R.string.correct_comment, Toast.LENGTH_LONG).show();
            }
            else {
                Log.d(TAG, "onPostExecute: false");
            }
            return ;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }


    public String readStream(InputStream inputStream) {
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
