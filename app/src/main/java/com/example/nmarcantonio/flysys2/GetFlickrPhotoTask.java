package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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


    private Context context;

    public GetFlickrPhotoTask(Context context, CardView cardView) {
        this.context = context;
        this.cardView = cardView;
    }

    private CardView cardView;
    private ImageView imageView;

    public GetFlickrPhotoTask(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        String ret = null, order;
        try {

            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=e3dae01fb6981aeab9b4b352ceb8a59a&text="+strings[0]+"&sort=interestingness-desc&format=json&nojsoncallback=1");

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
                        .showImageOnLoading(R.drawable.ic_loading)
                        .showImageOnFail(R.drawable.ic_error)     //bajar iconos
                        .build();
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                        .defaultDisplayImageOptions(defaultOptions)
                        .build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(config);
                FlickrImg item = imgs.get(0);

                if(imageView != null)
                 imageLoader.displayImage("http://farm"+ item.getFarm() +".static.flickr.com/"+ item.getServer() +"/"+ item.getId() +"_"+ item.getSecret() +"_m.jpg", imageView);


                else
                    imageLoader.loadImage("http://farm"+ item.getFarm() +".static.flickr.com/"+ item.getServer() +"/"+ item.getId() +"_"+ item.getSecret() +"_m.jpg", new SimpleImageLoadingListener(){

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);

                        cardView.setBackground(new BitmapDrawable(loadedImage));
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
