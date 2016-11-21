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
            holder.imageView = (ImageView) convertView.findViewById(R.id.offer_photo);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.offer_info);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.offer_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);

        new GetFlickrPhotoTask(convertView.getContext(),  (ImageView)convertView.findViewById(R.id.offer_photo)).execute(product.getName().split(",")[0].replaceAll(" ",""));
        // Comentar la línea anterior y descomentar la siguiente para ver de que manera el framework muestra una imagen alternativa al no encontrar la solicitada.
        //imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back2.jpg", imageView);
       // holder.imageView.setImageResource(R.drawable.ic_menu_white);
        holder.nameTextView.setText(product.getName());
        Double price = product.getPrice();
        holder.priceTextView.setText(price.toString());

        return convertView;
    }
}
