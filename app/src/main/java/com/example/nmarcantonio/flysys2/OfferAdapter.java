package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nmarcantonio on 18/11/16.
 */

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private List<Product> offerList;
    private Activity act;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView priceTextView;


        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.offer_photo2);
            nameTextView = (TextView) view.findViewById(R.id.offer_info2);
            priceTextView = (TextView) view.findViewById(R.id.offer_price2);
        }
    }


    public OfferAdapter(List<Product> offerList,Activity act) {
        this.offerList = offerList;
        this.act = act;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product prod = offerList.get(position);
        String [] city = prod.getName().split(",");
        if(city.length == 2)
             holder.nameTextView.setText(prod.getName());
        else
            holder.nameTextView.setText(city[0]+","+city[2]);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(act);
        holder.priceTextView.setText("$"+prefs.getString("money_list","USD")+" "+prod.getPrice());




        ImageView imageView = (ImageView)   holder.imageView;

        new GetFlickrPhotoTask(holder.imageView.getContext(),  imageView).execute(prod.getName().split(",")[0].replaceAll(" ",""));
        // Comentar la línea anterior y descomentar la siguiente para ver de que manera el framework muestra una imagen alternativa al no encontrar la solicitada.
        //imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back2.jpg", imageView);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}



