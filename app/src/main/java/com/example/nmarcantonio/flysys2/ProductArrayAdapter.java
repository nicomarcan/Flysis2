package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.URLEncoder;

public class ProductArrayAdapter extends ArrayAdapter<Product> {

    private Activity act;
    private View view;
    public ProductArrayAdapter(Activity context, Product[] objects,View view) {
        super(context, R.layout.list_view_item, objects);
        act = context;
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
        holder = new ViewHolder();
        holder.imageView = (CardView) convertView.findViewById(R.id.card_view);
        holder.nameTextView = (TextView) convertView.findViewById(R.id.offer_info);
        holder.priceTextView = (TextView) convertView.findViewById(R.id.offer_price);
        convertView.setTag(holder);


        Product product = getItem(position);
       // holder.imageView.setBackground(ContextCompat.getDrawable(act, R.drawable.ic_loading));
        try {
            new GetFlickrPhotoTask(act, (CardView) convertView.findViewById(R.id.card_view),(ProgressBar)convertView.findViewById(R.id.offers_progress_bar),view).execute(product.getName().replace(","," ").replace(" ","+"), product.getId());
        }catch(Exception e){
            ;
        }
            // Comentar la línea anterior y descomentar la siguiente para ver de que manera el framework muestra una imagen alternativa al no encontrar la solicitada.
        //imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back2.jpg", imageView);
       // holder.imageView.setImageResource(R.drawable.ic_menu_white);
        holder.nameTextView.setText(product.getName().split(",")[0]);

        String price = String.format ("%.2f", product.getPrice());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        holder.priceTextView.setText("$"+prefs.getString("money_list","USD")+" "+price);

        return convertView;
    }
}
