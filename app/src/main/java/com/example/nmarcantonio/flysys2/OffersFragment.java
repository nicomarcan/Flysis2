package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by nmarcantonio on 17/11/16.
 */

public class OffersFragment extends Fragment {

    View myView;
    final static String DEALS_NAME = "deals";
    private Activity context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new HttpGetTask().execute();
        myView = inflater.inflate(R.layout.offers_layout, container, false);

        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.map);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                */Intent intent = new Intent(context, OffersMap.class);
                startActivity(intent);
            }
        });
    }

    private class HttpGetTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("http://hci.it.itba.edu.ar/v1/api/booking.groovy?method=getflightdeals&from=BUE");
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
                final ListView listView = (ListView) myView.findViewById(R.id.list_view);
                // Toast.makeText(context,"HOLAAA",Toast.LENGTH_LONG).show();
                if (listView != null) {
                    final Product[] values = new Product[dealList.size()];

                    for (int j = 0; j <dealList.size(); j++) {
                        values[j] = new Product(j, dealList.get(j).getName(), new Double(dealList.get(j).getPrice() ) );
                    }

                    ;

                    ProductArrayAdapter adapter = new ProductArrayAdapter(context  , values);
                    listView.setAdapter(adapter);



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

}
