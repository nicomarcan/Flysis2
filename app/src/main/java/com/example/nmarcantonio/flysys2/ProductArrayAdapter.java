package com.example.nmarcantonio.flysys2;

import android.app.Activity;
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
    public ProductArrayAdapter(Activity context, Product[] objects) {
        super(context, R.layout.list_view_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.offer_photo);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.offer_info);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.offer_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.offer_photo);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                //.showImageOnLoading(R.drawable.ic_loading)
                //.showImageOnFail(R.drawable.ic_error)     //bajar iconos
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back.jpg", imageView);
        // Comentar la línea anterior y descomentar la siguiente para ver de que manera el framework muestra una imagen alternativa al no encontrar la solicitada.
        //imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back2.jpg", imageView);
       // holder.imageView.setImageResource(R.drawable.ic_menu_white);
        holder.nameTextView.setText(product.getName());
        Double price = product.getPrice();
        holder.priceTextView.setText(price.toString());

        return convertView;
    }
}
