package com.example.nmarcantonio.flysys2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.imageView.setImageResource(R.drawable.ic_menu_white);
        holder.nameTextView.setText(product.getName());
        Double price = product.getPrice();
        holder.priceTextView.setText(price.toString());

        return convertView;
    }
}
