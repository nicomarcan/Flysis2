package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class OfferInfoAdapter extends ArrayAdapter<OfferInfo> {


    public OfferInfoAdapter(Activity context, OfferInfo[] objects) {
        super(context, R.layout.list_view_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       OfferInfoViewHolder holder;
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_list_item, parent, false);
            holder = new OfferInfoViewHolder();
            holder.flightView = (TextView) convertView.findViewById(R.id.offer_info_flight);
            holder.srcAirView = (TextView) convertView.findViewById(R.id.offer_info_source_air);
            holder.dstAirView =(TextView) convertView.findViewById(R.id.offer_info_dest_air) ;
            holder.priceView = (TextView) convertView.findViewById(R.id.offer_info_price);
            holder.depDateView =(TextView) convertView.findViewById(R.id.offer_info_dep_date) ;
            holder.arrDateView = (TextView) convertView.findViewById(R.id.offer_info_arr_date);
            holder.rating = (RatingBar)convertView.findViewById(R.id.flight_rating) ;
            holder.logo = (ImageView)convertView.findViewById((R.id.logo));


            convertView.setTag(holder);


        OfferInfo info = getItem(position);


        holder.flightView.setText(info.getId()+" "+info.getNumber());


        holder.srcAirView.setText(info.getSrcAir());

        holder.dstAirView.setText(info.getDstAir());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String price = String.format ("%.2f", info.getPrice());
        holder.priceView.setText("$"+prefs.getString("money_list","USD")+" "+price);
        holder.rating.setRating(info.getRating());
        holder.logo.setImageBitmap(CacheImages.getInstance().getLogos().get(info.getId()));

        holder.depDateView.setText(formatDate(info.getDepDate()));

        holder.arrDateView.setText(formatDate(info.getArrDate()));
        return convertView;
    }



    private static String formatDate(String og) {
        String formated = "";
        try {
            Date date;
            date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("es", "AR")).parse(og);

            String language = Locale.getDefault().getDisplayLanguage();
            if ("español".equals(language)) {
                formated = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss", new Locale("es", "AR")).format(date);
            }
            else {
                formated = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", new Locale("en", "US")).format(date);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formated;
    }
}