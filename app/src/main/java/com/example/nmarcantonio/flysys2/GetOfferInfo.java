package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
    private Integer maxDays;
    private Double ratio;
    public static ArrayList<OfferInfo> values = new ArrayList<OfferInfo>();


    public GetOfferInfo(Activity act, String currentCity, String destId, Double offerPrice,Double ratio) {
        this.act = act;
        this.currentCity = currentCity;
        this.destId = destId;
        this.offerPrice = offerPrice;
        this.ratio = ratio;
    }


    @Override
    protected String doInBackground(Integer... params) {

        HttpURLConnection urlConnection = null;

        Calendar c = Calendar.getInstance();
        days = params[0];
        maxDays = params[1];
        c.add(Calendar.DATE, days);


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());


        try {
            URL url;
            url= new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getonewayflights&from="+currentCity+"&to="+destId+"&dep_date="+formattedDate+"&adults=1&children=0&infants=0&min_price="+offerPrice/ratio+"&max_price="+offerPrice/ratio);
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
          //  Toast.makeText(act, currentCity+" "+destId+" "+offerPrice*ratio, Toast.LENGTH_SHORT).show();

            if (!obj.has(OffersFragment.FLIGHTS_NAME )){


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



            if(flightList.size()>0){

                Flight f = flightList.get(0);

                values.add( new OfferInfo(f.getId(),f.getNumber(),f.getsrcAir() ,f.getdstAir(),f.getPrice()*ratio ,f.getDepDate(),f.getArrDate() ));



            }

                if(days < maxDays)
                    new GetOfferInfo(act,currentCity,destId,offerPrice,ratio).execute(days+1,maxDays);
            else {
                    final ListView listView = (ListView) act.findViewById(R.id.offer_list_view);
                    OfferInfoAdapter adapter = new OfferInfoAdapter(act, values.toArray(new OfferInfo[values.size()]));

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(act, FlightActivity.class);
                            String id = ((TextView)view.findViewById(R.id.offer_info_airline_id)).getText().toString();
                            String number =((TextView)view.findViewById(R.id.offer_info_num)).getText().toString();;
                            intent.putExtra("id",id);
                            intent.putExtra("number",number);

                            PendingIntent pendingIntent =
                                    TaskStackBuilder.create(act)
                                            // add all of DetailsActivity's parents to the stack,
                                            // followed by DetailsActivity itself
                                            .addNextIntentWithParentStack(intent)
                                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(act);
                            builder.setContentIntent(pendingIntent);
                            act.startActivity(intent);
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