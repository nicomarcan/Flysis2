package com.example.nmarcantonio.flysys2;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nmarcantonio on 18/11/16.
 */

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private List<Product> offerList;

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


    public OfferAdapter(List<Product> offerList) {
        this.offerList = offerList;
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
        holder.nameTextView.setText(prod.getName());
        holder.priceTextView.setText(prod.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}



