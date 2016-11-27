package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * Created by Nicolas on 11/21/2016.
 */

public class GetFlickrPhotoTask extends AsyncTask<String, Void, String> {


    private Activity context;
    private String id;
    private ProgressBar bar;
    private View view;



    public GetFlickrPhotoTask(Activity context, CardView cardView,ProgressBar bar,View view) {
        this.context = context;
        this.cardView = cardView;
        this.bar = bar;
        this.view = view;
    }




    private CardView cardView;




    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        String ret = null, order;
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            id = strings[1];
            if(CacheImages.getInstance().getImagesMap().get(id) != null  || (networkInfo == null || !networkInfo.isConnected())){
                return null;
            }
            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=e3dae01fb6981aeab9b4b352ceb8a59a&tags=city,relax,hd,landscape&tag_mode=any&text="+strings[0]+"&sort=relevance&format=json&nojsoncallback=1&per_page=1");

            conn = (HttpURLConnection) new URL(url.toString()).openConnection();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            ret = readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected()){
                final SwipeRefreshLayout l = (SwipeRefreshLayout)context.findViewById(R.id.offer_refresh) ;
                l.setRefreshing(false);
                Snackbar.make(view, R.string.no_internet_msg, Snackbar.LENGTH_LONG).show();
                return;
            }

            if(result == null){
                if(cardView != null)
                 cardView.setBackground(new BitmapDrawable(CacheImages.getInstance().getImagesMap().get(id) ));
                bar.setVisibility(View.GONE);
                return;
            }
            JSONObject obj = new JSONObject(result);
            if (!obj.has("photos")) {

                return;
            }
            else {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<FlickrImg>>() {
                }.getType();

                String jsonFragment = obj.getString("photos");
                 obj = new JSONObject(jsonFragment);
                 jsonFragment = obj.getString("photo");

                ArrayList<FlickrImg> imgs = gson.fromJson(jsonFragment, listType);


                DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .showImageOnFail(R.drawable.ic_error)     //bajar iconos
                        .build();
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                        .defaultDisplayImageOptions(defaultOptions)
                        .build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                if(!imageLoader.isInited())
                     imageLoader.init(config);
                FlickrImg item = imgs.get(0);


                        imageLoader.loadImage("http://farm" + item.getFarm() + ".static.flickr.com/" + item.getServer() + "/" + item.getId() + "_" + item.getSecret() + "_z.jpg", new SimpleImageLoadingListener() {

                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                CacheImages.getInstance().getImagesMap().put(id, loadedImage);
                                cardView.setBackground(new BitmapDrawable(loadedImage));
                                bar.setVisibility(View.GONE);

                            }

                        });



            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
            Log.d("err", "fallo la conexion");
            return null;
        }
    }

    private static void setText(Activity activity, int id, String str) {
        if (str == null) {
            str = "-";
        }
        ((TextView) activity.findViewById(id))
                .setText(str);
    }
}
