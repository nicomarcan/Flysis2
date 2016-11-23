package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_list_item, parent, false);
            holder = new OfferInfoViewHolder();
            holder.numberView = (TextView) convertView.findViewById(R.id.offer_info_num);
            holder.idView =(TextView) convertView.findViewById(R.id.offer_info_airline_id) ;
            holder.srcAirView = (TextView) convertView.findViewById(R.id.offer_info_source_air);
            holder.dstAirView =(TextView) convertView.findViewById(R.id.offer_info_dest_air) ;
            holder.priceView = (TextView) convertView.findViewById(R.id.offer_info_price);
            holder.depDateView =(TextView) convertView.findViewById(R.id.offer_info_dep_date) ;
            holder.arrDateView = (TextView) convertView.findViewById(R.id.offer_info_arr_date);


            convertView.setTag(holder);
        } else {
            holder = (OfferInfoViewHolder) convertView.getTag();
        }

        OfferInfo info = getItem(position);


        holder.numberView.setText(info.getNumber());

        holder.idView.setText(info.getId());

        holder.srcAirView.setText(info.getSrcAir());

        holder.dstAirView.setText(info.getDstAir());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String price = String.format ("%.2f", info.getPrice());
        holder.priceView.setText("$"+prefs.getString("money_list","USD")+" "+price);

        holder.depDateView.setText(info.getDepDate());

        holder.arrDateView.setText(info.getArrDate());
        return convertView;
    }
}