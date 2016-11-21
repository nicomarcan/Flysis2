package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class GetOfferInfo extends AsyncTask<Integer, Void, String> {

    private Activity act;
    private String currentCity;
    private String destId;
    private Double offerPrice;
    private Integer days;


    public GetOfferInfo(Activity act, String currentCity, String destId, Double offerPrice) {
        this.act = act;
        this.currentCity = currentCity;
        this.destId = destId;
        this.offerPrice = offerPrice;
    }


    @Override
    protected String doInBackground(Integer... params) {

        HttpURLConnection urlConnection = null;

        Calendar c = Calendar.getInstance();
        days = params[0];
        c.add(Calendar.DATE, days);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());


        try {
            URL url;
            if(offerPrice != null)
                url= new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getonewayflights&from="+currentCity+"&to="+destId+"&dep_date="+formattedDate+"&adults=1&children=0&infants=0&min_price="+offerPrice+"&max_price="+offerPrice);
            else
                url= new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getonewayflights&from="+currentCity+"&to="+destId+"&dep_date="+formattedDate+"&adults=1&children=0&infants=0");
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


            if (!obj.has(OffersFragment.FLIGHTS_NAME )){
                if(days < 9)
                    new GetOfferInfo(act,currentCity,destId,offerPrice).execute(days+1);
                return;
            }



            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Flight>>() {
            }.getType();

            String jsonFragment = obj.getString(OffersFragment.FLIGHTS_NAME);

            //Toast.makeText(context,jsonFragment,Toast.LENGTH_LONG).show();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(act);
            // Toast.makeText(context, prefs.getString("interval_list",""), Toast.LENGTH_LONG).show();


            ArrayList<Flight> flightList = gson.fromJson(jsonFragment, listType);



            OfferInfo[] values = new OfferInfo[1];
            if(flightList.size()>0){
                final ListView listView = (ListView) act.findViewById(R.id.offer_list_view);
                Flight f = flightList.get(0);

                values[0]=new OfferInfo(f.getId(),f.getNumber(),f.getsrcAir() ,f.getdstAir(),f.getPrice() ,f.getDepDate(),f.getArrDate()  );
                OfferInfoAdapter adapter = new OfferInfoAdapter(act  ,values);
                listView.setAdapter(adapter);


            }else{
                //Toast.makeText(context,"cabe",Toast.LENGTH_SHORT).show();
                if(days < 9)
                    new GetOfferInfo(act,currentCity,destId,offerPrice).execute(days+1);
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