package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class GetOffersToDestTask extends AsyncTask<String,Void, String> {

    private String destCity;
    private Activity act;
    private String currentCity;

    public GetOffersToDestTask(Activity act) {
        this.act = act;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
         currentCity=params[0];
         destCity = params[1];



        try {

            //URL url= new URL("hci.it.itba.edu.ar/v1/api/geo.groovy?method=getcitiesbyposition&latitude="+loc.getLatitude()+"&longitude="+loc.getLongitude()+"&radius=100");
            URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getlastminuteflightdeals&from="+currentCity);
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
            //Toast.makeText(act,result,Toast.LENGTH_LONG).show();
            if (!obj.has(OffersFragment.DEALS_NAME)) {
                return;
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Deal>>() {
            }.getType();

            String jsonFragment = obj.getString(OffersFragment.DEALS_NAME);


            ArrayList<Deal> dealList = gson.fromJson(jsonFragment, listType);
            for(Deal d : dealList){
                if(d.getId().equals(destCity)){
                    new GetOfferInfo(act,currentCity,destCity,new Double(d.getPrice())).execute(2);
                }
            }



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
