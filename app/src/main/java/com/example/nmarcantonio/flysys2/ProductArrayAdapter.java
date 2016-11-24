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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProductArrayAdapter extends ArrayAdapter<Product> {

    private Activity act;
    public ProductArrayAdapter(Activity context, Product[] objects) {
        super(context, R.layout.list_view_item, objects);
        act = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (CardView) convertView.findViewById(R.id.card_view);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.offer_info);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.offer_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);
        holder.imageView.setBackground(ContextCompat.getDrawable(act, R.drawable.ic_loading));
        new GetFlickrPhotoTask(convertView.getContext(),  (CardView) convertView.findViewById(R.id.card_view)).execute(product.getName().split(",")[0].replaceAll(" ",""));
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
