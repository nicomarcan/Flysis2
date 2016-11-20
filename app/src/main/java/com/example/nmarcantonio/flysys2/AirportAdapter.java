package com.example.nmarcantonio.flysys2;

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

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.AirportViewHolder> {

    private List<Airport> airportList;

    public class AirportViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView cityTextView;

        public AirportViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.airport_photo);
            nameTextView = (TextView) view.findViewById(R.id.airport_info);
            cityTextView = (TextView) view.findViewById(R.id.airport_info_2);
        }
    }


    public AirportAdapter(List<Airport> airportList) {
        this.airportList = airportList;
    }

    @Override
    public AirportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.airport_view_item, parent, false);

        return new AirportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AirportViewHolder holder, int position) {
        Airport airport = airportList.get(position);
        String [] splitdescr = airport.getDescription().split(",");

        holder.nameTextView.setText(splitdescr[0]);
        holder.cityTextView.setText(splitdescr[1] + ", " + splitdescr[2]);




        ImageView imageView = (ImageView)   holder.imageView;

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                //.showImageOnLoading(R.drawable.ic_loading)
                //.showImageOnFail(R.drawable.ic_error)     //bajar iconos
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(imageView.getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back.jpg", imageView);
        // Comentar la línea anterior y descomentar la siguiente para ver de que manera el framework muestra una imagen alternativa al no encontrar la solicitada.
        //imageLoader.displayImage("http://itba.edu.ar/sites/default/themes/itba/assets/images/back2.jpg", imageView);
    }

    @Override
    public int getItemCount() {
        return airportList.size();
    }
}



