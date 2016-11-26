package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
 * Created by Nicolas on 11/25/2016.
 */

public class GetAirlinesLogos extends AsyncTask<Void,Void,String> {
    private Activity act;

    public GetAirlinesLogos(Activity act){
        this.act = act;
    }
    @Override
    protected String doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;

        try {
            URL url;
            url= new URL("http://hci.it.itba.edu.ar/v1/api/misc.groovy?method=getairlines&page_size=1000");
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
            if (!obj.has("airlines" )){
                return;
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<AirlineInfo>>() {}.getType();
            String jsonFragment = obj.getString("airlines");
            ArrayList<AirlineInfo> airlines = gson.fromJson(jsonFragment, listType);
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.ic_loading)
                    .showImageOnFail(R.drawable.ic_error)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(act)
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            if(!imageLoader.isInited())
                imageLoader.init(config);
            for(AirlineInfo a : airlines ){
                final String id = a.getId();
                imageLoader.loadImage(a.getLogo(), new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        CacheImages.getInstance().getLogos().put(id, loadedImage);
                    }

                });

            }



        } catch (Exception exception) {

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
