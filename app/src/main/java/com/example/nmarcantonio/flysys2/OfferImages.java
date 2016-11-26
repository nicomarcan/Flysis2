package com.example.nmarcantonio.flysys2;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Nicolas on 11/24/2016.
 */

public class OfferImages {
    private HashMap<String,Bitmap> images;
    private static OfferImages instance;

    private OfferImages(){
        images = new HashMap<String, Bitmap>();
    }

    public static OfferImages getInstance(){
        if(instance == null){
            instance= new OfferImages();
        }
            return instance;
    }

    public HashMap<String,Bitmap> getImagesMap(){
        return images;
    }
}
