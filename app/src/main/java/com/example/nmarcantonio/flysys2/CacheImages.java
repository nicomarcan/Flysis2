package com.example.nmarcantonio.flysys2;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Nicolas on 11/24/2016.
 */

public class CacheImages {
    private HashMap<String,Bitmap> images;
    private static CacheImages instance;
    private HashMap<String,Bitmap> logos;

    private CacheImages(){
        images = new HashMap<String, Bitmap>();
        logos = new HashMap<String,Bitmap>();
    }

    public static CacheImages getInstance(){
        if(instance == null){
            instance= new CacheImages();
        }
            return instance;
    }

    public HashMap<String,Bitmap> getImagesMap(){
        return images;
    }

    public HashMap<String, Bitmap> getLogos() {
        return logos;
    }
}
